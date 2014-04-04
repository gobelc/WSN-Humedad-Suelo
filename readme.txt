    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.


##### RSI 2012 ##############################################################################################
Proyecto de fin de curso:
Red inalámbrica de medición de humedad en Suelo
2012

Estudiantes:
Gonzalo Belcredi
Emilio Font

Tutor:
Pablo Mazzara
#############################################################################################################

INSTRUCCIONES (para LINUX)

1. Compilar un mote como nodo base, para ello abrir en terminal el directorio /motes/BaseSuelos y ejecutar:

make telosb install.0


2. Compilar motes como nodos sensores, para ello abrir en terminal el directorio /motes/SensorRemoto y ejecutar:

make telosb install.x   (con x cualquier número distinto de 0)


3. Correr JHumedadSuelos, para ello, abrir en terminal el directorio JHumedadSuelos y ejecutar:

java -jar JHumedadSuelos.jar


