"use client"

import type React from "react"

import Image from "next/image"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { ShoppingCart, Heart } from "lucide-react"
import type { Product } from "@/lib/types"
import { useCart } from "@/contexts/CartContext"
import { useAuth } from "@/contexts/AuthContext"
import { useToast } from "@/hooks/use-toast"

interface ProductCardProps {
  product: Product
}

export function ProductCard({ product }: ProductCardProps) {
  const { addToCart } = useCart()
  const { user } = useAuth()
  const { toast } = useToast()

  // Debug logging
  console.log("ProductCard received product:", product)

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()

    if (!user) {
      toast({
        title: "Please login",
        description: "You need to be logged in to add items to cart",
        variant: "destructive",
      })
      return
    }

    await addToCart(product.id, 1)
  }

  const discountPercentage =
    product.salePrice && product.basePrice && product.salePrice < product.basePrice
      ? Math.round(((product.basePrice - product.salePrice) / product.basePrice) * 100)
      : 0

  return (
    <Link href={`/products/${product.id}`}>
      <Card className="group hover:shadow-lg transition-shadow duration-200 cursor-pointer">
        <CardContent className="p-4">
          <div className="relative aspect-square mb-4 overflow-hidden rounded-lg bg-muted">
            <Image
              src={product.imageUrl || `/placeholder.svg?height=300&width=300&text=${encodeURIComponent(product.name)}`}
              alt={product.name}
              fill
              className="object-cover group-hover:scale-105 transition-transform duration-200"
            />
            {product.isFeatured && <Badge className="absolute top-2 left-2">Featured</Badge>}
            {discountPercentage > 0 && (
              <Badge variant="destructive" className="absolute top-2 right-2">
                -{discountPercentage}%
              </Badge>
            )}
            <Button
              size="icon"
              variant="secondary"
              className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity"
              onClick={(e) => {
                e.preventDefault()
                e.stopPropagation()
                // Add to wishlist functionality
              }}
            >
              <Heart className="h-4 w-4" />
            </Button>
          </div>

          <div className="space-y-2">
            <h3 className="font-semibold line-clamp-2 group-hover:text-primary transition-colors">{product.name}</h3>
            <p className="text-sm text-muted-foreground line-clamp-2">{product.description}</p>
            <div className="flex items-center space-x-2">
              <span className="font-bold text-lg">${product.salePrice?.toFixed(2) || '0.00'}</span>
              {product.salePrice && product.basePrice && product.salePrice < product.basePrice && (
                <span className="text-sm text-muted-foreground line-through">${product.basePrice.toFixed(2)}</span>
              )}
            </div>
          </div>
        </CardContent>

        <CardFooter className="p-4 pt-0">
          <Button className="w-full" onClick={handleAddToCart} disabled={!product.isActive}>
            <ShoppingCart className="mr-2 h-4 w-4" />
            Add to Cart
          </Button>
        </CardFooter>
      </Card>
    </Link>
  )
}
