# Environment Configuration

This project uses environment variables to manage sensitive configuration data. Follow these steps to set up your environment:

## Setup Instructions

1. **Copy the example environment file:**
   ```bash
   cp .env.example .env
   ```

2. **Edit the `.env` file** and replace the placeholder values with your actual configuration:
   - Database credentials
   - Email service credentials
   - Stripe API keys
   - JWT secret key
   - Admin credentials

## Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_URL` | PostgreSQL database URL | `jdbc:postgresql://localhost:5432/ecommerce_db` |
| `DB_USERNAME` | Database username | `myuser` |
| `DB_PASSWORD` | Database password | `mypassword` |
| `MAIL_USERNAME` | Email service username | `your_email@gmail.com` |
| `MAIL_PASSWORD` | Email service password/app password | `your_password` |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `secure_password` |
| `APP_BASE_URL` | Application base URL | `http://localhost:8080` |
| `STRIPE_SECRET_KEY` | Stripe secret key | `sk_test_...` |
| `STRIPE_PUBLIC_KEY` | Stripe public key | `pk_test_...` |
| `STRIPE_WEBHOOK_SECRET` | Stripe webhook secret | `whsec_...` |
| `JWT_SECRET` | JWT signing secret | `your_secure_jwt_secret` |
| `JWT_EXPIRATION` | JWT token expiration time (ms) | `86400000` |
| `GEMINI_API_KEY` | Gemini AI API key | `your_gemini_key` |

## Security Notes

- Never commit the `.env` file to version control
- The `.env.example` file should contain placeholder values only
- Use strong, unique passwords and secrets
- Rotate secrets regularly in production

## Spring Boot Configuration

The application uses Spring Boot's environment variable support with fallback defaults defined in `application.yml`. Environment variables take precedence over the default values.
