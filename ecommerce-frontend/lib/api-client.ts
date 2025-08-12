import axios from "axios"

export const apiClient = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
})

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("auth_token")
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Only clear token and redirect if not already on auth pages
      const currentPath = window.location.pathname
      if (!currentPath.startsWith('/auth/')) {
        localStorage.removeItem("auth_token")
        window.location.href = "/auth/login"
      }
    }
    return Promise.reject(error)
  },
)
