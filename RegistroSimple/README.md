# Ejercicio de Repaso para el examen de Programación Móvil y Multimedia
> Alejandro del Valle Vallés

## Enunciado
Crear una aplicación sencilla de Registro Civil. La aplicación consta de 5 pantallas.
1. Presentación: Muestra una imagen del registro civil y dos botones "Iniciar Sesión" y "Registrarse"
2. Reistrarse: Muestra un formulario a rellenar con el nombre, apellidos, email, contraseña, fecha de nacimiento y si acepta los términos y condiciones.<br>
Tiene que tener 2 botones. "Registrase" que solo podrá ejecutarse cuando los datos estén correctamente rellenados (No se debe permitir nombres vacíos, fechas futuras...).<br>
Y "Salir" que debe volver a la pantalla de inicio.
3. Una vez registrado, debe mostrar una pantalla con su Nombre y Apellidos indicándole que se ha registrado. Tiene que haber un botón de "Salir" que debe devolver a la pantalla de inicio.
4. Iniciar Sesión: Hasta que no se haya registrado no debe permitir iniciar sesión, debe inidcarle que no hay un usuario registrado. Debe dejarle acceder a esta pnatalla pero no iniciar sesión.<br>
Si tiene un usuario registrado, debe introducir el mismo email y contrseña que puso en el registro.
Debe de tener dos botones, "Inciar Sesión" y "Salir". El primero debe comprobar que los datos son correctos y si lo son, lanzar la siguiente actividad. El de salir, debe devolverle a la pantalla de inicio.
5. Información: Una vez haya iniciado sesión, debe lanzar una nueva pantalla (Actividad) mostrando el nombre, apellidos, email, edad (Calculado) y fecha de nacimiento con un mensaje de "Bienvenido".<br>
Debe haber un botón de "Borrar Usuario" que debe borrar todo y devolverle a la pantlla principal  mostrandole en un mensajito que ha sido eliminado correctamente.<br>
Y otro botón que sea "Cerrar Sesión" que debe devolverle a la pantalla principal y mostrarle un mensajito que diga "Sesión Cerrada".

Se debe usar los elementos más adecuados para cada cosa y el programa debe compilar correctamente. Debes hacer un diseño medianamente aceptable, poniendo los botones de salir en rojo y los<br>
que lancen nuevas pantllas en cualquiero otro color que no sea rojo.
La pantalla de inicio debe contar con otra tipografía diferente a la estbelcida por defecto.