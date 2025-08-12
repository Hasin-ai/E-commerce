import { Header } from "@/components/layout/Header"
import { Footer } from "@/components/layout/Footer"
import { OrderHistory } from "@/components/orders/OrderHistory"
import { ProtectedRoute } from "@/components/auth/ProtectedRoute"

export default function OrdersPage() {
  return (
    <ProtectedRoute>
      <div className="min-h-screen flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 py-8">
          <h1 className="text-3xl font-bold mb-8">Order History</h1>
          <OrderHistory />
        </main>
        <Footer />
      </div>
    </ProtectedRoute>
  )
}
