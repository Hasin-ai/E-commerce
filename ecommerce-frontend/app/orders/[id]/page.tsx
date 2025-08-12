import { Header } from "@/components/layout/Header"
import { Footer } from "@/components/layout/Footer"
import { OrderDetails } from "@/components/orders/OrderDetails"
import { ProtectedRoute } from "@/components/auth/ProtectedRoute"

interface OrderPageProps {
  params: {
    id: string
  }
}

export default function OrderPage({ params }: OrderPageProps) {
  return (
    <ProtectedRoute>
      <div className="min-h-screen flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 py-8">
          <OrderDetails orderId={params.id} />
        </main>
        <Footer />
      </div>
    </ProtectedRoute>
  )
}
