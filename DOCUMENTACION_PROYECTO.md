# Documentación Técnica - Proyecto DataClassApp

## 1. Resumen de la Actividad 6: Pruebas Funcionales

Se han realizado pruebas funcionales de extremo a extremo (End-to-End) para validar que todas las operaciones principales de la aplicación funcionan correctamente en el dispositivo, asegurando un flujo de usuario sin errores técnicos.

### Casos de Prueba Ejecutados (Operaciones CRUD y Autenticación)

| Funcionalidad | Descripción de la Prueba | Resultado |
| :--- | :--- | :--- |
| **Autenticación** | Inicio de sesión con credenciales de usuario desde la pantalla de Login conectada a Firebase Auth. | **EXITOSO** |
| **Registro de Usuario** | Creación de una cuenta nueva para acceso al sistema. | **EXITOSO** |
| **Creación (Agregar)** | Registro de un nuevo producto incluyendo nombre, descripción, precio, stock y selección de imagen desde la cámara o galería. | **EXITOSO** |
| **Visualización (Lista)** | Verificación de que el producto aparece en la lista principal con su información y miniatura de imagen cargada correctamente. | **EXITOSO** |
| **Edición (Actualizar)** | Modificación de los datos de un producto existente (ej. cambio de nombre o precio) y guardado de cambios. | **EXITOSO** |
| **Eliminación (Borrar)** | Borrado físico del producto y limpieza automática del archivo de imagen asociado en el dispositivo. | **EXITOSO** |

---

## 2. Evidencia de Ejecución (Resultados)

Los resultados detallados de cada operación funcional se encuentran documentados en los archivos situados en la raíz del proyecto:

1. **`EVIDENCIA_PRUEBAS_FUNCIONALES.txt`**: Registro detallado (Log) que muestra el paso a paso de cada acción realizada durante las pruebas y las rutas de archivos generadas.
2. **`reporte_pruebas_funcionales.txt`**: Resumen ejecutivo con el estado final de las pruebas de sistema y aceptación.

Este reporte confirma que:
- El sistema de **almacenamiento local** gestiona correctamente las imágenes sin depender de configuraciones externas de red.
- Las operaciones de la base de datos se reflejan instantáneamente en la interfaz de usuario.
- La aplicación maneja correctamente el flujo de permisos (Cámara) sin presentar cierres inesperados.

---

## 3. Justificación de Permisos Android

Para que la aplicación sea operativa y cumpla con sus funciones, se implementaron los siguientes permisos:

1. **`android.permission.CAMERA`**: Necesario para capturar fotos de productos en tiempo real. 
2. **`android.permission.INTERNET`**: Crucial para la comunicación con la base de datos en la nube (Firestore).
3. **`android.permission.ACCESS_NETWORK_STATE`**: Permite a la app optimizar las operaciones según el estado de la conexión.

---

## 4. Resolución Técnica (Gestión de Imágenes)
Se resolvió el error 404 detectado inicialmente mediante un cambio funcional hacia **almacenamiento interno local**. Ahora, la app copia las imágenes a una carpeta privada del dispositivo, garantizando que el usuario siempre pueda gestionar sus fotos de forma rápida, segura y sin depender del servidor de imágenes de Firebase.

---

## 5. Uso de Retrofit
Aunque la persistencia principal se realiza en Firebase, el proyecto incluye **Retrofit** configurado. Esta librería está lista para escalar la aplicación y conectar con APIs REST externas si el negocio lo requiere en futuras fases de desarrollo.
