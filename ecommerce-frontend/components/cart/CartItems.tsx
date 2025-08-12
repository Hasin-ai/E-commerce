"use client"

import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { Trash2, Plus, Minus } from "lucide-react"
import { useCart } from "@/contexts/CartContext"
import { LoadingSpinner } from "@/components/ui/loading-spinner"

export function CartItems() {
  const { cart, loading, updateQuantity, removeItem } = useCart()

  if (loading) {
    return <LoadingSpinner />
  }

  if (!cart || cart.items.length === 0) {
    return (
      <Card>
        <CardContent className="p-8 text-center">
          <p className="text-muted-foreground">Your cart is empty</p>
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="space-y-4">
      {cart.items.map((item) => (
        <Card key={item.id}>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="relative h-20 w-20 flex-shrink-0">
                <Image
                  src={
                    item.productImageUrl ||
                    `/placeholder.svg?height=80&width=80&text=${encodeURIComponent(item.productName)}`
                  }
                  alt={item.productName}
                  fill
                  className="object-cover rounded-md"
                />
              </div>

              <div className="flex-1 min-w-0">
                <h3 className="font-semibold truncate">{item.productName}</h3>
                <p className="text-sm text-muted-foreground">${item.unitPrice?.toFixed(2) || '0.00'} each</p>
              </div>

              <div className="flex items-center space-x-2">
                <Button
                  variant="outline"
                  size="icon"
                  onClick={() => updateQuantity(item.id, Math.max(1, item.quantity - 1))}
                  disabled={item.quantity <= 1}
                >
                  <Minus className="h-4 w-4" />
                </Button>
                <Input
                  type="number"
                  min="1"
                  value={item.quantity}
                  onChange={(e) => {
                    const newQuantity = Math.max(1, Number.parseInt(e.target.value) || 1)
                    updateQuantity(item.id, newQuantity)
                  }}
                  className="w-16 text-center"
                />
                <Button variant="outline" size="icon" onClick={() => updateQuantity(item.id, item.quantity + 1)}>
                  <Plus className="h-4 w-4" />
                </Button>
              </div>

              <div className="text-right">
                <p className="font-semibold">${item.totalPrice?.toFixed(2) || '0.00'}</p>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => removeItem(item.id)}
                  className="text-destructive hover:text-destructive"
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}
