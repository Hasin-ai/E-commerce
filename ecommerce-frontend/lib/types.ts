export interface Product {
  id: number
  name: string
  slug: string
  description: string
  sku: string
  basePrice: number
  imageUrl?: string
  stockQuantity: number
  createdAt: string
  updatedAt: string
}

// API Response wrapper that matches backend ApiResponse
export interface ApiResponse<T> {
  success: boolean
  data: T
  message: string
  timestamp: string
}
// Paginated response structure that matches backend
export interface PagedResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
  numberOfElements: number
}

// Authentication response structure
export interface AuthResponse {
  accessToken: string
  tokenType: string
  expiresIn: number
  user: User
}

// User interface that matches backend UserResponseDto
export interface User {
  id: number
  firstName: string
  lastName: string
  email: string
  phone: string
  isActive: boolean
  isEmailVerified: boolean
  createdAt: string
  updatedAt: string
}

export interface CartItem {
  id: number
  productId: number
  productName: string
  productImageUrl: string
  quantity: number
  unitPrice: number
  totalPrice: number
}

// Cart response structure that matches backend
export interface Cart {
  cartId: number
  userId: number
  items: CartItem[]
  totalItems: number
  totalPrice: number
  updatedAt: string
}

export interface Order {
  id: number
  orderNumber: string
  userId: number
  status: string
  totalAmount: number
  subtotalAmount: number
  taxAmount: number
  shippingAmount: number
  discountAmount: number
  currency: string
  items: OrderItem[]
  shippingAddress: Address
  statusHistory?: StatusHistory[]
  trackingNumber?: string
  createdAt: string
  updatedAt: string
}

export interface OrderItem {
  id: number
  productId: number
  productName: string
  productSku: string
  quantity: number
  unitPrice: number
  totalPrice: number
}

export interface Address {
  street: string
  city: string
  state: string
  zipCode: string
  country: string
}

export interface StatusHistory {
  status: string
  timestamp: string
}
  isActive: boolean
  isFeatured: boolean
  imageUrl: string
  category: string
  stockQuantity?: number
