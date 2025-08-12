"use client"

import { useEffect, useState } from "react"
import { ProductCard } from "./ProductCard"
import { LoadingSpinner } from "@/components/ui/loading-spinner"
import { apiClient } from "@/lib/api-client"
import type { Product } from "@/lib/types"

interface RelatedProductsProps {
  productId: string
}

export function RelatedProducts({ productId }: RelatedProductsProps) {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchRelatedProducts = async () => {
      try {
        // Fetch recommendations or similar products
        const response = await apiClient.get("/search/recommendations?limit=4")
        if (response.data.success) {
          // Convert recommendations to products (simplified)
          const relatedProducts = response.data.data.slice(0, 4)
          setProducts(relatedProducts)
        }
      } catch (error) {
        console.error("Failed to fetch related products:", error)
        // Fallback: fetch random products
        try {
          const fallbackResponse = await apiClient.get("/products?size=4")
          if (fallbackResponse.data.success) {
            setProducts(fallbackResponse.data.data.content)
          }
        } catch (fallbackError) {
          console.error("Failed to fetch fallback products:", fallbackError)
        }
      } finally {
        setLoading(false)
      }
    }

    fetchRelatedProducts()
  }, [productId])

  if (loading) {
    return <LoadingSpinner />
  }

  if (products.length === 0) {
    return null
  }

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Related Products</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  )
}
