import Link from "next/link"
import { Card, CardContent } from "@/components/ui/card"
import { Laptop, Smartphone, Headphones, Watch, Camera, Gamepad2 } from "lucide-react"

const categories = [
  {
    name: "Electronics",
    icon: Laptop,
    href: "/products?category=electronics",
    description: "Latest gadgets and devices",
  },
  {
    name: "Mobile Phones",
    icon: Smartphone,
    href: "/products?category=mobile",
    description: "Smartphones and accessories",
  },
  {
    name: "Audio",
    icon: Headphones,
    href: "/products?category=audio",
    description: "Headphones and speakers",
  },
  {
    name: "Wearables",
    icon: Watch,
    href: "/products?category=wearables",
    description: "Smart watches and fitness trackers",
  },
  {
    name: "Cameras",
    icon: Camera,
    href: "/products?category=cameras",
    description: "Digital cameras and lenses",
  },
  {
    name: "Gaming",
    icon: Gamepad2,
    href: "/products?category=gaming",
    description: "Gaming consoles and accessories",
  },
]

export function ProductCategories() {
  return (
    <section className="py-16 bg-muted/50">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold mb-4">Shop by Category</h2>
          <p className="text-muted-foreground max-w-2xl mx-auto">
            Explore our wide range of product categories and find exactly what you're looking for
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {categories.map((category) => {
            const Icon = category.icon
            return (
              <Link key={category.name} href={category.href}>
                <Card className="hover:shadow-lg transition-shadow cursor-pointer group">
                  <CardContent className="p-6 text-center">
                    <div className="mb-4">
                      <Icon className="h-12 w-12 mx-auto text-primary group-hover:scale-110 transition-transform" />
                    </div>
                    <h3 className="font-semibold text-lg mb-2">{category.name}</h3>
                    <p className="text-sm text-muted-foreground">{category.description}</p>
                  </CardContent>
                </Card>
              </Link>
            )
          })}
        </div>
      </div>
    </section>
  )
}
