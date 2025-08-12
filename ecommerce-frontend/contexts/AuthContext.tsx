"use client"

import { createContext, useContext, useEffect, useState, type ReactNode } from "react"
import { apiClient } from "@/lib/api-client"
import type { User, AuthResponse, ApiResponse } from "@/lib/types"

interface AuthContextType {
  user: User | null
  token: string | null
  login: (email: string, password: string) => Promise<void>
  register: (userData: RegisterData) => Promise<void>
  logout: () => Promise<void>
  loading: boolean
}

interface RegisterData {
  firstName: string
  lastName: string
  email: string
  password: string
  phone: string
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const storedToken = localStorage.getItem("auth_token")
    if (storedToken) {
      setToken(storedToken)
      fetchUserProfile(storedToken)
    } else {
      setLoading(false)
    }
  }, [])

  const fetchUserProfile = async (authToken: string) => {
    try {
      const response = await apiClient.get<ApiResponse<User>>("/users/profile", {
        headers: { Authorization: `Bearer ${authToken}` },
      })
      if (response.data.success) {
        setUser(response.data.data)
      }
    } catch (error) {
      console.error("Failed to fetch user profile:", error)
      localStorage.removeItem("auth_token")
      setToken(null)
    } finally {
      setLoading(false)
    }
  }

  const login = async (email: string, password: string) => {
    const response = await apiClient.post<ApiResponse<AuthResponse>>("/auth/login", { email, password })
    if (response.data.success) {
      const { accessToken, user: userData } = response.data.data
      setToken(accessToken)
      setUser(userData)
      localStorage.setItem("auth_token", accessToken)
    } else {
      throw new Error(response.data.message || "Login failed")
    }
  }

  const register = async (userData: RegisterData) => {
    const response = await apiClient.post<ApiResponse<User>>("/auth/register", userData)
    if (response.data.success) {
      // Auto-login after registration
      await login(userData.email, userData.password)
    } else {
      throw new Error(response.data.message || "Registration failed")
    }
  }

  const logout = async () => {
    console.log("Logout initiated")
    try {
      // Call the backend logout endpoint (now exists)
      if (token) {
        console.log("Calling server-side logout")
        await apiClient.post<ApiResponse<null>>("/auth/logout", {}, {
          headers: { Authorization: `Bearer ${token}` },
        }).catch((error) => {
          // If logout endpoint fails, we still proceed with client-side logout
          console.warn("Server-side logout failed, proceeding with client-side logout", error)
        })
      }
    } catch (error) {
      console.error("Logout error:", error)
    } finally {
      // Always clear client-side state regardless of server response
      console.log("Clearing client-side auth state")
      setUser(null)
      setToken(null)
      localStorage.removeItem("auth_token")
      console.log("Logout completed")
    }
  }

  return (
    <AuthContext.Provider value={{ user, token, login, register, logout, loading }}>{children}</AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}
