# Documentación de la API de AppTareas

Esta es la documentación para la API de AppTareas.

---

## Autenticación

La mayoría de las rutas requieren un ID de usuario (`idUsuario`) como parte de la URL para identificar al usuario que realiza la acción.

---

## Controlador de Usuarios (`/api/usuarios`)

Este controlador maneja la creación, eliminación y login de usuarios.

### 1. Crear un nuevo usuario

- **Método:** `POST`
- **Ruta:** `/api/usuarios/crearUsuario`
- **Descripción:** Registra un nuevo usuario en el sistema.
- **Cuerpo de la Solicitud (`application/json`):**

| Campo | Tipo | Descripción | Validaciones |
| :--- | :--- | :--- | :--- |
| `nombre` | String | Nombre del usuario. | No puede estar vacío, máximo 20 caracteres. |
| `email` | String | Correo electrónico del usuario. | Debe ser un formato de email válido y no puede estar vacío. |
| `contraseña` | String | Contraseña para el acceso. | No puede estar vacía, mínimo 6 caracteres. |

- **Ejemplo de Solicitud:**
```json
{
  "nombre": "Juan Perez",
  "email": "juan.perez@example.com",
  "contraseña": "password123"
}
```

- **Respuesta Exitosa (`200 OK`):**
```json
{
    "id": 1,
    "nombre": "Juan Perez",
    "email": "juan.perez@example.com",
    "tareas": []
}
```
- **Respuestas de Error:**
  - `400 Bad Request`: Si los datos de entrada no son válidos.

### 2. Iniciar sesión

- **Método:** `POST`
- **Ruta:** `/api/usuarios/login`
- **Descripción:** Autentica a un usuario y devuelve sus datos si las credenciales son correctas.
- **Cuerpo de la Solicitud (`application/json`):**

| Campo | Tipo | Descripción | Validaciones |
| :--- | :--- | :--- | :--- |
| `email` | String | Correo electrónico del usuario. | No puede estar vacío, debe ser un email válido. |
| `contraseña` | String | Contraseña del usuario. | No puede estar vacía. |

- **Ejemplo de Solicitud:**
```json
{
  "email": "juan.perez@example.com",
  "contraseña": "password123"
}
```

- **Respuesta Exitosa (`200 OK`):**
```json
{
    "id": 1,
    "nombre": "Juan Perez",
    "email": "juan.perez@example.com",
    "tareas": [
    ]
}
```
- **Respuestas de Error:**
  - `400 Bad Request`: Si el email o la contraseña son incorrectos o no cumplen la validación.

### 3. Eliminar un usuario

- **Método:** `DELETE`
- **Ruta:** `/api/usuarios/eliminarUsuario/{id}`
- **Descripción:** Elimina el perfil de un usuario por su ID.
- **Parámetros de Ruta:**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | Long | El ID del usuario a eliminar. |

- **Respuesta Exitosa:**
  - `204 No Content`: Si el usuario se elimina correctamente.
- **Respuestas de Error:**
  - `404 Not Found`: Si no se encuentra un usuario con el ID proporcionado.

---

## Controlador de Tareas (`/api/tareas`)

Maneja todas las operaciones CRUD para las tareas de un usuario.

### 1. Crear una nueva tarea

- **Método:** `POST`
- **Ruta:** `/api/tareas/crearTarea/{idUsuario}`
- **Descripción:** Crea una nueva tarea y la asocia a un usuario.
- **Parámetros de Ruta:**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `idUsuario` | Long | El ID del usuario al que se le asignará la tarea. |

- **Cuerpo de la Solicitud (`application/json`):**

| Campo | Tipo | Descripción | Validaciones |
| :--- | :--- | :--- | :--- |
| `titulo` | String | Título de la tarea. | No puede estar vacío, máximo 50 caracteres. |
| `descripcion` | String | Descripción detallada de la tarea. | No puede estar vacía. |
| `recordatorio` | String | Fecha y hora para un recordatorio. | Formato `yyyy-MM-dd HH:mm`. No puede ser nulo. |
| `estadoTarea` | Boolean | Indica si la tarea está completada o no. | `true` o `false`. |

- **Ejemplo de Solicitud:**
```json
{
  "titulo": "Hacer la compra",
  "descripcion": "Comprar leche, huevos y pan.",
  "recordatorio": "2025-11-15 10:00",
  "estadoTarea": false
}
```

- **Respuesta Exitosa (`200 OK`):**
```json
{
    "id": 101,
    "titulo": "Hacer la compra",
    "descripcion": "Comprar leche, huevos y pan.",
    "recordatorio": "2025-11-15T10:00:00",
    "estadoTarea": false
}
```
- **Respuestas de Error:**
  - `400 Bad Request`: Si los datos de la tarea no son válidos.
  - `404 Not Found`: Si el `idUsuario` no existe.

### 2. Actualizar una tarea

- **Método:** `PUT`
- **Ruta:** `/api/tareas/actualizarTarea/{idUsuario}/{idTarea}`
- **Descripción:** Actualiza los detalles de una tarea existente.
- **Parámetros de Ruta:**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `idUsuario` | Long | El ID del usuario propietario de la tarea. |
| `idTarea` | Long | El ID de la tarea a actualizar. |

- **Cuerpo de la Solicitud (`application/json`):** (Solo incluye los campos que deseas modificar)

| Campo | Tipo | Descripción | Validaciones |
| :--- | :--- | :--- | :--- |
| `titulo` | String | Nuevo título de la tarea. | Opcional. |
| `descripcion` | String | Nueva descripción de la tarea. | Opcional. |
| `recordatorio`| String | Nueva fecha de recordatorio. | Formato `yyyy-MM-dd HH:mm`, debe ser en el presente o futuro. Opcional. |
| `estadoTarea` | Boolean | Nuevo estado de la tarea. | Opcional. |

- **Ejemplo de Solicitud:**
```json
{
  "estadoTarea": true,
  "descripcion": "Tarea completada."
}
```

- **Respuesta Exitosa (`200 OK`):**
```json
{
    "id": 101,
    "titulo": "Hacer la compra",
    "descripcion": "Tarea completada.",
    "recordatorio": "2025-11-15T10:00:00",
    "estadoTarea": true
}
```
- **Respuestas de Error:**
  - `404 Not Found`: Si el `idUsuario` o `idTarea` no existen.

### 3. Eliminar una tarea

- **Método:** `DELETE`
- **Ruta:** `/api/tareas/eliminarTarea/{idUsuario}/{idTarea}`
- **Descripción:** Elimina una tarea específica de un usuario.
- **Parámetros de Ruta:**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `idUsuario` | Long | El ID del usuario. |
| `idTarea` | Long | El ID de la tarea a eliminar. |

- **Respuesta Exitosa:**
  - `204 No Content`: Si la tarea se elimina correctamente.
- **Respuestas de Error:**
  - `404 Not Found`: Si el `idUsuario` o `idTarea` no se encuentran.

### 4. Listar todas las tareas de un usuario

- **Método:** `GET`
- **Ruta:** `/api/tareas/listaTareas/{idUsuario}`
- **Descripción:** Devuelve una lista con todas las tareas de un usuario.
- **Parámetros de Ruta:**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `idUsuario` | Long | El ID del usuario. |

- **Respuesta Exitosa (`200 OK`):**
```json
[
    {
        "id": 101,
        "titulo": "Hacer la compra",
        "descripcion": "Comprar leche, huevos y pan.",
        "recordatorio": "2025-11-15T10:00:00",
        "estadoTarea": false
    },
    {
        "id": 102,
        "titulo": "Llamar al médico",
        "descripcion": "Pedir cita para la revisión anual.",
        "recordatorio": "2025-11-20T15:30:00",
        "estadoTarea": false
    }
]
```
- **Respuestas de Error:**
  - `404 Not Found`: Si el `idUsuario` no existe.

---

## Controlador de IA (`/api/ia`)

Este controlador proporciona una interfaz para interactuar con un servicio de IA (ChatGPT).

### 1. Realizar una consulta a la IA

- **Método:** `POST`
- **Ruta:** `/api/ia/{idUsuario}`
- **Descripción:** Envía una pregunta a un modelo de lenguaje y devuelve la respuesta. La consulta puede estar relacionada con las tareas del usuario.
- **Parámetros de Ruta:**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `idUsuario` | Long | El ID del usuario que realiza la consulta. |

- **Cuerpo de la Solicitud (`application/json`):**

| Campo | Tipo | Descripción | Validaciones |
| :--- | :--- | :--- | :--- |
| `texto` | String | La pregunta o el texto a enviar al modelo de IA. | No puede estar vacío. |

- **Ejemplo de Solicitud:**
```json
{
  "texto": "¿Puedes darme ideas para organizar mis tareas de la semana?"
}
```

- **Respuesta Exitosa (`200 OK`):**
```json
{
    "pregunta": "¿Puedes darme ideas para organizar mis tareas de la semana?",
    "respuesta": "Claro, aquí tienes algunas ideas: 1. Prioriza tus tareas usando la matriz de Eisenhower..."
}
```
- **Respuestas de Error:**
  - `400 Bad Request`: Si el texto de la consulta está vacío o si ocurre un error al comunicarse con el servicio de IA.
  - `404 Not Found`: Si el `idUsuario` no existe.
