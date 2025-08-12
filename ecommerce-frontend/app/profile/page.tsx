import { Header } from "@/components/layout/Header"
import { Footer } from "@/components/layout/Footer"
import { ProfileInfo } from "@/components/profile/ProfileInfo"
import { ProtectedRoute } from "@/components/auth/ProtectedRoute"

export default function ProfilePage() {
  return (
    <ProtectedRoute>
      <div className="min-h-screen flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 py-8">
          <h1 className="text-3xl font-bold mb-8">My Profile</h1>
          <ProfileInfo />
        </main>
        <Footer />
      </div>
    </ProtectedRoute>
  )
}
