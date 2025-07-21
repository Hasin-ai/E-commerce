#!/bin/bash
# Fix build script for E-commerce application

echo "🔧 Fixing E-commerce build issues..."

# Step 1: Clean everything
echo "📁 Cleaning project..."
mvn clean

# Step 2: Delete IDE cache directories
echo "🗑️  Removing IDE cache..."
rm -rf .idea/
rm -rf .vscode/
rm -rf target/
rm -rf .metadata/

# Step 3: Force update all dependencies
echo "⬇️  Updating dependencies..."
mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false
mvn dependency:resolve-sources
mvn dependency:resolve

# Step 4: Compile with fresh cache
echo "🔨 Compiling project..."
mvn clean compile -U

# Step 5: Verify compilation
echo "✅ Verifying compilation..."
if [ $? -eq 0 ]; then
    echo "✅ Build successful! Project is ready to run."
    echo "🚀 To start the application: mvn spring-boot:run"
else
    echo "❌ Build failed. Check the output above for errors."
fi

echo "📋 Build fix complete!"
