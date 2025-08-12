"use client"

import { createContext, useContext, useEffect, useState, type ReactNode } from "react"
import { apiClient } from "@/lib/api-client"
import { useAuth } from "./AuthContext"
import { useToast } from "@/hooks/use-toast"
import type { Cart, CartItem, ApiResponse } from "@/lib/types"

interface CartContextType {
  cart: Cart | null
  loading: boolean
  addToCart: (productId: number, quantity: number) => Promise<void>
  updateQuantity: (itemId: number, quantity: number) => Promise<void>
  removeItem: (itemId: number) => Promise<void>
  clearCart: () => Promise<void>
  refreshCart: () => Promise<void>
}

const CartContext = createContext<CartContextType | undefined>(undefined)

export function CartProvider({ children }: { children: ReactNode }) {
  const [cart, setCart] = useState<Cart | null>(null)
  const [loading, setLoading] = useState(false)
  const { token, user } = useAuth()
  const { toast } = useToast()

  useEffect(() => {
    if (token && user) {
      refreshCart()
    } else {
      // Clear cart when user logs out
      setCart(null)
    }
  }, [token, user])

  const refreshCart = async () => {
    if (!token) return

    try {
      setLoading(true)
      const response = await apiClient.get<ApiResponse<Cart>>("/cart", {
        headers: { Authorization: `Bearer ${token}` },
      })
      if (response.data.success) {
        console.log("Cart API Response:", response.data.data)
        setCart(response.data.data)
      } else {
        console.error("Cart API returned unsuccessful response:", response.data)
      }
    } catch (error) {
      console.error("Failed to fetch cart:", error)
    } finally {
      setLoading(false)
    }
  }

  const addToCart = async (productId: number, quantity: number) => {
    if (!token) {
      toast({
        title: "Please login",
        description: "You need to be logged in to add items to cart",
        variant: "destructive",
      })
      return
    }

    try {
      const response = await apiClient.post<ApiResponse<Cart>>(
        "/cart/items",
        { productId, quantity },
        { headers: { Authorization: `Bearer ${token}` } },
      )
      if (response.data.success) {
        console.log("Add to cart API Response:", response.data.data)
        setCart(response.data.data)
        toast({
          title: "Added to cart",
          description: "Item has been added to your cart",
        })
      } else {
        console.error("Add to cart API returned unsuccessful response:", response.data)
        toast({
          title: "Error",
          description: response.data.message || "Failed to add item to cart",
          variant: "destructive",
        })
      }
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to add item to cart",
        variant: "destructive",
      })
    }
  }

  const updateQuantity = async (itemId: number, quantity: number) => {
    if (!token) return

    try {
      const response = await apiClient.put<ApiResponse<Cart>>(
        `/cart/items/${itemId}`,
        { quantity },
        { headers: { Authorization: `Bearer ${token}` } },
      )
      if (response.data.success) {
        setCart(response.data.data)
        toast({
          title: "Cart updated",
          description: "Item quantity has been updated",
        })
      }
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to update quantity",
        variant: "destructive",
      })
    }
  }

  const removeItem = async (itemId: number) => {
    if (!token) return

    try {
      const response = await apiClient.delete<ApiResponse<Cart>>(`/cart/items/${itemId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      if (response.data.success) {
        setCart(response.data.data)
        toast({
          title: "Item removed",
          description: "Item has been removed from your cart",
        })
      }
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to remove item",
        variant: "destructive",
      })
    }
  }

  const clearCart = async () => {
    if (!token) return

    try {
      const response = await apiClient.delete<ApiResponse<void>>("/cart", {
        headers: { Authorization: `Bearer ${token}` },
      })
      if (response.data.success) {
        setCart(null)
        toast({
          title: "Cart cleared",
          description: "All items have been removed from your cart",
        })
      }
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to clear cart",
        variant: "destructive",
      })
    }
  }

  return (
    <CartContext.Provider
      value={{
        cart,
        loading,
        addToCart,
        updateQuantity,
        removeItem,
        clearCart,
        refreshCart,
      }}
    >
      {children}
    </CartContext.Provider>
  )
}

export function useCart() {
  const context = useContext(CartContext)
  if (context === undefined) {
    throw new Error("useCart must be used within a CartProvider")
  }
  return context
}
