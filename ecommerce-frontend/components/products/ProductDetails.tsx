"use client"

import { useEffect, useState } from "react"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { ShoppingCart, Heart, Minus, Plus, Truck, Shield, RotateCcw } from "lucide-react"
import { LoadingSpinner } from "@/components/ui/loading-spinner"
import { apiClient } from "@/lib/api-client"
import type { Product } from "@/lib/types"
import { useCart } from "@/contexts/CartContext"
import { useAuth } from "@/contexts/AuthContext"
import { useToast } from "@/hooks/use-toast"

interface ProductDetailsProps {
  productId: string
}

export function ProductDetails({ productId }: ProductDetailsProps) {
  const [product, setProduct] = useState<Product | null>(null)
  const [loading, setLoading] = useState(true)
  const [quantity, setQuantity] = useState(1)
  const { addToCart } = useCart()
  const { user } = useAuth()
  const { toast } = useToast()

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await apiClient.get(`/products/${productId}`)
        if (response.data.success) {
          setProduct(response.data.data)
        }
      } catch (error) {
        console.error("Failed to fetch product:", error)
        toast({
          title: "Error",
          description: "Failed to load product details",
          variant: "destructive",
        })
      } finally {
        setLoading(false)
      }
    }

    fetchProduct()
  }, [productId, toast])

  const handleAddToCart = async () => {
    if (!user) {
      toast({
        title: "Please login",
        description: "You need to be logged in to add items to cart",
        variant: "destructive",
      })
      return
    }

    if (product) {
      await addToCart(product.id, quantity)
    }
  }

  const incrementQuantity = () => {
    setQuantity((prev) => prev + 1)
  }

  const decrementQuantity = () => {
    setQuantity((prev) => Math.max(1, prev - 1))
  }

  if (loading) {
    return <LoadingSpinner />
  }

  if (!product) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">Product not found.</p>
      </div>
    )
  }

  const discountPercentage =
    product.salePrice && product.basePrice && product.salePrice < product.basePrice
      ? Math.round(((product.basePrice - product.salePrice) / product.basePrice) * 100)
      : 0

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
      {/* Product Images */}
      <div className="space-y-4">
        <div className="aspect-square relative overflow-hidden rounded-lg bg-muted">
          <Image
            src={product.imageUrl || `/placeholder.svg?height=600&width=600&text=${encodeURIComponent(product.name)}`}
            alt={product.name}
            fill
            className="object-cover"
            priority
          />
          {product.isFeatured && <Badge className="absolute top-4 left-4">Featured</Badge>}
          {discountPercentage > 0 && (
            <Badge variant="destructive" className="absolute top-4 right-4">
              -{discountPercentage}%
            </Badge>
          )}
        </div>
      </div>

      {/* Product Info */}
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold mb-2">{product.name}</h1>
          <p className="text-muted-foreground mb-4">{product.description}</p>

          <div className="flex items-center space-x-4 mb-4">
            <div className="flex items-center space-x-2">
              <span className="text-3xl font-bold">${product.salePrice?.toFixed(2) || '0.00'}</span>
              {product.salePrice && product.basePrice && product.salePrice < product.basePrice && (
                <span className="text-xl text-muted-foreground line-through">${product.basePrice.toFixed(2)}</span>
              )}
            </div>
            {discountPercentage > 0 && <Badge variant="destructive">Save {discountPercentage}%</Badge>}
          </div>

          <div className="flex items-center space-x-4 text-sm text-muted-foreground">
            <span>SKU: {product.sku}</span>
            <span>Category: {product.category}</span>
            {product.stockQuantity !== undefined && (
              <span className={product.stockQuantity > 0 ? "text-green-600" : "text-red-600"}>
                {product.stockQuantity > 0 ? "In Stock" : "Out of Stock"}
              </span>
            )}
          </div>
        </div>

        <Separator />

        {/* Quantity and Add to Cart */}
        <div className="space-y-4">
          <div className="flex items-center space-x-4">
            <Label htmlFor="quantity">Quantity:</Label>
            <div className="flex items-center space-x-2">
              <Button variant="outline" size="icon" onClick={decrementQuantity} disabled={quantity <= 1}>
                <Minus className="h-4 w-4" />
              </Button>
              <Input
                id="quantity"
                type="number"
                min="1"
                value={quantity}
                onChange={(e) => setQuantity(Math.max(1, Number.parseInt(e.target.value) || 1))}
                className="w-20 text-center"
              />
              <Button variant="outline" size="icon" onClick={incrementQuantity}>
                <Plus className="h-4 w-4" />
              </Button>
            </div>
          </div>

          <div className="flex space-x-4">
            <Button
              size="lg"
              className="flex-1"
              onClick={handleAddToCart}
              disabled={!product.isActive || (product.stockQuantity !== undefined && product.stockQuantity <= 0)}
            >
              <ShoppingCart className="mr-2 h-5 w-5" />
              Add to Cart
            </Button>
            <Button size="lg" variant="outline">
              <Heart className="mr-2 h-5 w-5" />
              Wishlist
            </Button>
          </div>
        </div>

        <Separator />

        {/* Features */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <Card>
            <CardContent className="p-4 text-center">
              <Truck className="h-8 w-8 mx-auto mb-2 text-primary" />
              <h3 className="font-semibold text-sm">Free Shipping</h3>
              <p className="text-xs text-muted-foreground">On orders over $50</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-4 text-center">
              <Shield className="h-8 w-8 mx-auto mb-2 text-primary" />
              <h3 className="font-semibold text-sm">Warranty</h3>
              <p className="text-xs text-muted-foreground">1 year guarantee</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-4 text-center">
              <RotateCcw className="h-8 w-8 mx-auto mb-2 text-primary" />
              <h3 className="font-semibold text-sm">Easy Returns</h3>
              <p className="text-xs text-muted-foreground">30 day return policy</p>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
