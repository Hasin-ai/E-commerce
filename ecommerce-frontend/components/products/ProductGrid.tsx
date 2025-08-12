"use client"

import { useEffect, useState } from "react"
import { useSearchParams } from "next/navigation"
import { ProductCard } from "./ProductCard"
import { LoadingSpinner } from "@/components/ui/loading-spinner"
import { Button } from "@/components/ui/button"
import { apiClient } from "@/lib/api-client"
import type { Product, ApiResponse, PagedResponse } from "@/lib/types"

export function ProductGrid() {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [hasMore, setHasMore] = useState(false)
  const searchParams = useSearchParams()

  useEffect(() => {
    fetchProducts(0, true)
  }, [searchParams])

  const fetchProducts = async (pageNum: number, reset = false) => {
    try {
      setLoading(true)
      const params = new URLSearchParams()
      params.set("page", pageNum.toString())
      params.set("size", "12")

      // Add search and filter parameters
      const search = searchParams.get("search")
      const category = searchParams.get("category")
      const featured = searchParams.get("featured")

      if (search) params.set("search", search)
      if (category) params.set("category", category)
      if (featured) params.set("featured", featured)

      const response = await apiClient.get<ApiResponse<PagedResponse<Product>>>(`/products?${params.toString()}`)

      if (response.data.success) {
        console.log("Products API Response:", response.data.data)
        const newProducts = response.data.data.content
        setProducts(reset ? newProducts : [...products, ...newProducts])
        setTotalPages(response.data.data.totalPages)
        setHasMore(pageNum + 1 < response.data.data.totalPages)
        setPage(pageNum)
      } else {
        console.error("API returned unsuccessful response:", response.data)
      }
    } catch (error) {
      console.error("Failed to fetch products:", error)
    } finally {
      setLoading(false)
    }
  }

  const loadMore = () => {
    fetchProducts(page + 1, false)
  }

  if (loading && products.length === 0) {
    return <LoadingSpinner />
  }

  if (products.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">No products found.</p>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>

      {hasMore && (
        <div className="text-center">
          <Button onClick={loadMore} disabled={loading} variant="outline">
            {loading ? "Loading..." : "Load More"}
          </Button>
        </div>
      )}
    </div>
  )
}
