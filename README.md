# 🔥 Parrillada Los 2 Carnales

Aplicación móvil Android desarrollada para gestionar la experiencia de compra de una parrillada, desde la exploración del menú hasta el seguimiento de pedidos, junto con un módulo administrativo destinado a la gestión del negocio.

El proyecto está desarrollado utilizando **Kotlin y Jetpack Compose**, aplicando una arquitectura organizada por capas para separar la interfaz, la lógica de negocio y el acceso a datos.

## 🧐Integrantes
-Victor Manuel Frias
-Ramon Junior Ureña

## 🎞️Video Promocional
https://youtu.be/ZH6_FPBRCVc?si=STz4pZm4nIuAgGoQ

---

## 📱 Funcionalidades

### 👤 Cliente

* Inicio de sesión y registro de usuarios.
* Pantalla principal con categorías, platos y ofertas.
* Exploración del menú por categorías.
* Visualización detallada de cada plato.
* Selección del término de la carne.
* Selección de salsa y guarnición.
* Manejo de salsas y guarniciones adicionales con costo extra.
* Visualización de ofertas disponibles.
* Carrito de compras.
* Resumen y cálculo del pedido.
* Flujo demostrativo de pago.
* Confirmación del pedido.
* Seguimiento del estado del pedido.
* Historial de pedidos por usuario.
* Sistema de notificaciones.
* Perfil y edición de datos del cliente.
* Compatibilidad visual con modo claro y oscuro.

### 🛠️ Administrador

* Dashboard administrativo.
* Gestión de platos.
* Gestión de guarniciones.
* Gestión de componentes del menú.
* Gestión de ofertas.
* Gestión y seguimiento de pedidos.
* Sistema de notificaciones.
* Perfil del administrador.
* Interfaz adaptada a los temas claro y oscuro.

---

## 🧰 Tecnologías

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **Navigation Compose / Navigation 3**
* **Room**
* **Hilt**
* **Kotlin Coroutines**
* **Firebase Authentication**
* **Google Sign-In / Credential Manager**
* **Coil**
* **Kotlin Serialization**
* **KSP**
* **JUnit**
* **MockK**
* **Turbine**
* **Robolectric**
* **Compose UI Test**

---

## 🏗️ Arquitectura

El proyecto mantiene separadas las diferentes responsabilidades de la aplicación.

```text
app/
└── src/main/java/com/edu/ucne/parrilladalos2carnales/
    ├── data/
    │   ├── local/
    │   ├── repository/
    │   └── mapper/
    │
    ├── domain/
    │   ├── model/
    │   ├── repository/
    │   └── useCase/
    │
    ├── di/
    │
    └── presentacion/
        ├── login/
        ├── inicio/
        ├── menu/
        ├── carrito/
        ├── pago/
        ├── historial/
        ├── perfil/
        ├── navigation/
        └── administrador/
```

Esta organización permite mantener la lógica de negocio separada de la interfaz y facilita el mantenimiento, las pruebas y futuras ampliaciones del proyecto.

---

## 🧭 Flujo principal del cliente

```text
Login / Registro
        ↓
      Inicio
        ↓
 Menú / Categorías
        ↓
 Detalle del plato
        ↓
 Personalización
        ↓
     Carrito
        ↓
      Pago
        ↓
 Confirmación
        ↓
 Seguimiento
        ↓
    Historial
```

---

## 💳 Pago

La aplicación incorpora la interfaz y el flujo necesario para realizar el proceso de compra.

Al tratarse de un proyecto académico, actualmente **no se utiliza una pasarela bancaria real**, por lo que el proceso de pago funciona como una demostración dentro de la aplicación sin procesar datos bancarios reales.

---

## 🔔 Notificaciones

La aplicación posee un sistema de notificaciones que permite mantener informado al usuario sobre eventos importantes relacionados con su experiencia y sus pedidos.

Las notificaciones se gestionan de forma asociada al usuario correspondiente.

---

## 🍽️ Personalización de platos

Los platos pueden incluir diferentes opciones según su configuración:

* Término de la carne.
* Salsa incluida.
* Guarnición incluida.
* Salsas adicionales.
* Guarniciones adicionales.

Cuando el cliente selecciona componentes adicionales a los incluidos en el servicio, el costo correspondiente se agrega automáticamente al total del pedido.

---

## 🎁 Ofertas

El administrador puede gestionar las ofertas disponibles en la aplicación.

Las ofertas activas pueden ser mostradas al cliente desde la interfaz principal, permitiendo destacar promociones, combos y productos especiales.

---

## 🚀 Instalación

### Requisitos

* Android Studio.
* JDK compatible con el proyecto.
* Android SDK 24 o superior.
* Dispositivo Android o emulador.
* Configuración de Firebase para las funciones de autenticación.

### Clonar el repositorio

```bash
git clone https://github.com/Junior102010/ParrilladaLos2Carnales.git
```

Luego:

```bash
cd ParrilladaLos2Carnales
```

Abre el proyecto utilizando **Android Studio**, espera la sincronización de Gradle y ejecuta la aplicación utilizando un dispositivo físico o un emulador.

---

## 🎯 Objetivo

Desarrollar una aplicación móvil moderna para una parrillada que permita al cliente consultar el menú, personalizar sus platos, aprovechar ofertas, realizar pedidos y consultar su historial.

Al mismo tiempo, proporcionar al administrador herramientas para gestionar los productos, componentes, ofertas y pedidos desde la misma aplicación.

---

## 📌 Estado del proyecto

El proyecto cuenta con los principales flujos del cliente y del administrador, entre ellos:

* Autenticación.
* Menú y categorías.
* Carrito.
* Personalización de platos.
* Pago.
* Confirmación y seguimiento.
* Historial.
* Perfil.
* Notificaciones.
* Administración de platos.
* Administración de componentes.
* Administración de ofertas.
* Administración de pedidos.

---

## 🎓 Proyecto académico

Proyecto Android desarrollado utilizando principios de separación de responsabilidades, manejo de estado mediante ViewModels, persistencia local y una interfaz construida utilizando Jetpack Compose.

**Parrillada Los 2 Carnales 🔥**
