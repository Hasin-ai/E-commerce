import Link from "next/link"
import { Facebook, Twitter, Instagram, Mail } from "lucide-react"

export function Footer() {
  return (
    <footer className="bg-muted/50 border-t">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Company Info */}
          <div className="space-y-4">
            <div className="flex items-center space-x-2">
              <div className="h-8 w-8 bg-primary rounded-md flex items-center justify-center">
                <span className="text-primary-foreground font-bold text-sm">E</span>
              </div>
              <span className="font-bold text-xl">E-Store</span>
            </div>
            <p className="text-sm text-muted-foreground">
              Your trusted online shopping destination for quality products at great prices.
            </p>
            <div className="flex space-x-4">
              <Facebook className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer" />
              <Twitter className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer" />
              <Instagram className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer" />
              <Mail className="h-5 w-5 text-muted-foreground hover:text-primary cursor-pointer" />
            </div>
          </div>

          {/* Quick Links */}
          <div className="space-y-4">
            <h3 className="font-semibold">Quick Links</h3>
            <div className="space-y-2">
              <Link href="/products" className="block text-sm text-muted-foreground hover:text-primary">
                All Products
              </Link>
              <Link href="/products?featured=true" className="block text-sm text-muted-foreground hover:text-primary">
                Featured
              </Link>
              <Link
                href="/products?category=electronics"
                className="block text-sm text-muted-foreground hover:text-primary"
              >
                Electronics
              </Link>
            </div>
          </div>

          {/* Customer Service */}
          <div className="space-y-4">
            <h3 className="font-semibold">Customer Service</h3>
            <div className="space-y-2">
              <Link href="/contact" className="block text-sm text-muted-foreground hover:text-primary">
                Contact Us
              </Link>
              <Link href="/shipping" className="block text-sm text-muted-foreground hover:text-primary">
                Shipping Info
              </Link>
              <Link href="/returns" className="block text-sm text-muted-foreground hover:text-primary">
                Returns
              </Link>
              <Link href="/faq" className="block text-sm text-muted-foreground hover:text-primary">
                FAQ
              </Link>
            </div>
          </div>

          {/* Account */}
          <div className="space-y-4">
            <h3 className="font-semibold">Account</h3>
            <div className="space-y-2">
              <Link href="/profile" className="block text-sm text-muted-foreground hover:text-primary">
                My Profile
              </Link>
              <Link href="/orders" className="block text-sm text-muted-foreground hover:text-primary">
                Order History
              </Link>
              <Link href="/cart" className="block text-sm text-muted-foreground hover:text-primary">
                Shopping Cart
              </Link>
            </div>
          </div>
        </div>

        <div className="border-t mt-8 pt-8 text-center">
          <p className="text-sm text-muted-foreground">© 2025 E-Store. All rights reserved.</p>
        </div>
      </div>
    </footer>
  )
}
