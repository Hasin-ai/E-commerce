import { Header } from "@/components/layout/Header"
import { Footer } from "@/components/layout/Footer"
import { CartItems } from "@/components/cart/CartItems"
import { CartSummary } from "@/components/cart/CartSummary"
import { ProtectedRoute } from "@/components/auth/ProtectedRoute"

export default function CartPage() {
  return (
    <ProtectedRoute>
      <div className="min-h-screen flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 py-8">
          <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2">
              <CartItems />
            </div>

            <div className="lg:col-span-1">
              <CartSummary />
            </div>
          </div>
        </main>
        <Footer />
      </div>
    </ProtectedRoute>
  )
}
