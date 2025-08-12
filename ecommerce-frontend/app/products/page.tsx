import { Suspense } from "react"
import { Header } from "@/components/layout/Header"
import { Footer } from "@/components/layout/Footer"
import { ProductGrid } from "@/components/products/ProductGrid"
import { ProductFilters } from "@/components/products/ProductFilters"
import { SearchBar } from "@/components/products/SearchBar"
import { LoadingSpinner } from "@/components/ui/loading-spinner"

// Create wrapper components for Suspense boundaries
function SearchBarWrapper() {
  return (
    <Suspense fallback={<div className="h-10 bg-gray-100 rounded animate-pulse" />}>
      <SearchBar />
    </Suspense>
  )
}

function ProductFiltersWrapper() {
  return (
    <Suspense fallback={<div className="h-64 bg-gray-100 rounded animate-pulse" />}>
      <ProductFilters />
    </Suspense>
  )
}

function ProductGridWrapper() {
  return (
    <Suspense fallback={<LoadingSpinner />}>
      <ProductGrid />
    </Suspense>
  )
}

export default function ProductsPage() {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-4">All Products</h1>
          <SearchBarWrapper />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          <aside className="lg:col-span-1">
            <ProductFiltersWrapper />
          </aside>

          <div className="lg:col-span-3">
            <ProductGridWrapper />
          </div>
        </div>
      </main>
      <Footer />
    </div>
  )
}
