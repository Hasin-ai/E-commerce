import { Suspense } from "react"
import { Header } from "@/components/layout/Header"
import { Footer } from "@/components/layout/Footer"
import { ProductDetails } from "@/components/products/ProductDetails"
import { RelatedProducts } from "@/components/products/RelatedProducts"
import { LoadingSpinner } from "@/components/ui/loading-spinner"

interface ProductPageProps {
  params: {
    id: string
  }
}

export default function ProductPage({ params }: ProductPageProps) {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <Suspense fallback={<LoadingSpinner />}>
          <ProductDetails productId={params.id} />
        </Suspense>

        <div className="mt-16">
          <Suspense fallback={<LoadingSpinner />}>
            <RelatedProducts productId={params.id} />
          </Suspense>
        </div>
      </main>
      <Footer />
    </div>
  )
}
