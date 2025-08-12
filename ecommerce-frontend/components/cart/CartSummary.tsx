"use client"

import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { useCart } from "@/contexts/CartContext"
import { useAuth } from "@/contexts/AuthContext"

export function CartSummary() {
  const { cart } = useCart()
  const { user } = useAuth()

  // Add debug logging
  console.log("CartSummary received cart:", cart)

  if (!cart || cart.items.length === 0) {
    return null
  }

  const subtotal = cart.totalPrice || 0
  const shipping = subtotal > 50 ? 0 : 10
  const tax = subtotal * 0.08 // 8% tax
  const total = subtotal + shipping + tax
  const totalItems = cart.totalItems || cart.items.length || 0

  return (
    <Card>
      <CardHeader>
        <CardTitle>Order Summary</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex justify-between">
          <span>Subtotal ({totalItems} items)</span>
          <span>${subtotal.toFixed(2)}</span>
        </div>

        <div className="flex justify-between">
          <span>Shipping</span>
          <span>{shipping === 0 ? "Free" : `$${shipping.toFixed(2)}`}</span>
        </div>

        <div className="flex justify-between">
          <span>Tax</span>
          <span>${tax.toFixed(2)}</span>
        </div>

        <Separator />

        <div className="flex justify-between font-semibold text-lg">
          <span>Total</span>
          <span>${total.toFixed(2)}</span>
        </div>

        {shipping > 0 && subtotal < 50 && (
          <p className="text-sm text-muted-foreground">Add ${(50 - subtotal).toFixed(2)} more for free shipping</p>
        )}

        <div className="space-y-2">
          {user ? (
            <Button asChild className="w-full" size="lg">
              <Link href="/checkout">Proceed to Checkout</Link>
            </Button>
          ) : (
            <Button asChild className="w-full" size="lg">
              <Link href="/auth/login?redirect=/checkout">Login to Checkout</Link>
            </Button>
          )}

          <Button variant="outline" asChild className="w-full bg-transparent">
            <Link href="/products">Continue Shopping</Link>
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
