#!/bin/bash
# Fix JDK configuration for IntelliJ IDEA

echo "🔧 Fixing JDK configuration for E-commerce project..."

# Check Java version
echo "📋 Current Java version:"
java -version
echo ""

# Check JAVA_HOME
echo "📋 JAVA_HOME environment variable:"
echo "JAVA_HOME: $JAVA_HOME"
echo ""

# Verify Maven Java version
echo "📋 Maven Java version:"
mvn -version
echo ""

echo "✅ JDK information collected. Please follow these IDE configuration steps:"
echo ""
echo "🔧 IntelliJ IDEA Configuration:"
echo "1. File → Project Structure (Ctrl+Alt+Shift+S)"
echo "2. Project Settings → Project"
echo "3. Set Project SDK to: Java 17 (or your installed JDK 17)"
echo "4. Set Project language level to: 17"
echo "5. Project Settings → Modules"
echo "6. Select 'E-commerce' module"
echo "7. Set Module SDK to: Project SDK (Java 17)"
echo "8. Apply and OK"
echo ""
echo "🔄 Alternative quick fix:"
echo "1. Right-click on project root"
echo "2. Open Module Settings"
echo "3. Set SDK to Java 17"
echo "4. Apply changes"
echo ""
echo "If Java 17 is not available in the list:"
echo "1. Go to File → Project Structure → Platform Settings → SDKs"
echo "2. Click '+' → Add JDK"
echo "3. Navigate to your JDK 17 installation (usually /usr/lib/jvm/java-17-openjdk-amd64)"
echo "4. Add and select it"
