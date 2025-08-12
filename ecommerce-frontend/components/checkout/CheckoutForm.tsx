"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Separator } from "@/components/ui/separator"
import { useCart } from "@/contexts/CartContext"
import { useAuth } from "@/contexts/AuthContext"
import { useToast } from "@/hooks/use-toast"
import { apiClient } from "@/lib/api-client"
import type { Address } from "@/lib/types"

export function CheckoutForm() {
  const [loading, setLoading] = useState(false)
  const [shippingAddress, setShippingAddress] = useState<Address>({
    street: "",
    city: "",
    state: "",
    zipCode: "",
    country: "US",
  })
  const [billingAddress, setBillingAddress] = useState<Address>({
    street: "",
    city: "",
    state: "",
    zipCode: "",
    country: "US",
  })
  const [sameAsShipping, setSameAsShipping] = useState(true)

  const { cart, refreshCart } = useCart()
  const { token } = useAuth()
  const { toast } = useToast()
  const router = useRouter()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!token || !cart) return

    setLoading(true)
    try {
      const orderData = {
        shippingAddress,
        billingAddress: sameAsShipping ? shippingAddress : billingAddress,
      }

      const response = await apiClient.post("/orders", orderData, {
        headers: { Authorization: `Bearer ${token}` },
      })

      if (response.data.success) {
        toast({
          title: "Order placed successfully!",
          description: `Order #${response.data.data.orderNumber} has been created.`,
        })

        // Clear cart and redirect to order confirmation
        await refreshCart()
        router.push(`/orders/${response.data.data.id}`)
      }
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.response?.data?.message || "Failed to place order",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }

  const updateShippingAddress = (field: keyof Address, value: string) => {
    setShippingAddress((prev) => ({ ...prev, [field]: value }))
  }

  const updateBillingAddress = (field: keyof Address, value: string) => {
    setBillingAddress((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Shipping Address */}
      <Card>
        <CardHeader>
          <CardTitle>Shipping Address</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label htmlFor="shipping-street">Street Address</Label>
            <Input
              id="shipping-street"
              value={shippingAddress.street}
              onChange={(e) => updateShippingAddress("street", e.target.value)}
              required
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label htmlFor="shipping-city">City</Label>
              <Input
                id="shipping-city"
                value={shippingAddress.city}
                onChange={(e) => updateShippingAddress("city", e.target.value)}
                required
              />
            </div>
            <div>
              <Label htmlFor="shipping-state">State</Label>
              <Input
                id="shipping-state"
                value={shippingAddress.state}
                onChange={(e) => updateShippingAddress("state", e.target.value)}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label htmlFor="shipping-zip">ZIP Code</Label>
              <Input
                id="shipping-zip"
                value={shippingAddress.zipCode}
                onChange={(e) => updateShippingAddress("zipCode", e.target.value)}
                required
              />
            </div>
            <div>
              <Label htmlFor="shipping-country">Country</Label>
              <Input
                id="shipping-country"
                value={shippingAddress.country}
                onChange={(e) => updateShippingAddress("country", e.target.value)}
                required
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Billing Address */}
      <Card>
        <CardHeader>
          <CardTitle>Billing Address</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              id="same-as-shipping"
              checked={sameAsShipping}
              onChange={(e) => setSameAsShipping(e.target.checked)}
              className="rounded"
            />
            <Label htmlFor="same-as-shipping">Same as shipping address</Label>
          </div>

          {!sameAsShipping && (
            <>
              <Separator />
              <div>
                <Label htmlFor="billing-street">Street Address</Label>
                <Input
                  id="billing-street"
                  value={billingAddress.street}
                  onChange={(e) => updateBillingAddress("street", e.target.value)}
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="billing-city">City</Label>
                  <Input
                    id="billing-city"
                    value={billingAddress.city}
                    onChange={(e) => updateBillingAddress("city", e.target.value)}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="billing-state">State</Label>
                  <Input
                    id="billing-state"
                    value={billingAddress.state}
                    onChange={(e) => updateBillingAddress("state", e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="billing-zip">ZIP Code</Label>
                  <Input
                    id="billing-zip"
                    value={billingAddress.zipCode}
                    onChange={(e) => updateBillingAddress("zipCode", e.target.value)}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="billing-country">Country</Label>
                  <Input
                    id="billing-country"
                    value={billingAddress.country}
                    onChange={(e) => updateBillingAddress("country", e.target.value)}
                    required
                  />
                </div>
              </div>
            </>
          )}
        </CardContent>
      </Card>

      <Button type="submit" size="lg" className="w-full" disabled={loading}>
        {loading ? "Placing Order..." : "Place Order"}
      </Button>
    </form>
  )
}
