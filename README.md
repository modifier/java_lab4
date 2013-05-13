java_lab4
=========

Лабораторная работа № 4 по программированию интернет-приложений. Вариант 152.

Компиляция и запуск
-------------------
Компиляция:

    $ javac *.java

Запуск:

    $ java MainController

Небольшое описание
------------------
Вставлять в каждый файл javadoc мне лень, поэтому краткое описание функций каждого класса тут.

* Mark — базовая модель точки. Также содержит метод isInside, который на основании заданного радиуса вычисляет, входит ли точка в указанную область.
* MarkCollection — композитная модель из множества точек. Также включает в себя значение радиуса. Также содержит метод forEach, который совершает итерации по множеству заданных точек при помощи метода интерфейса MarkIterator.
* MainController — точка доступа к программе. Инициализирует модель и представление. Реализует интерфейс INotifyable, к которому обращается класс Graphic для добавления точек при клике по графику.
* MainView — базовое представление. Инициализирует окно и его элементы. Метод newDots добавляет новые точки на основании выбранных.
* XPanel, YPanel и RadiusPanel — панели, содержащие элементы представления координат и радуса. Сделаны исключительно как абстракция над выбранными элементами доступа (в данном случае — JComboBox и JCheckBox). Методы getValues и getValue предоставляют выбранные юзером значения.
* Graphic — представление графика. Все основные константы приведены наверху. Данные о точках берутся из MarkCollection, само изображение графика находится в методе drawGraphic.

Реализация варианта
-------------------
В целом, я старался сделать все части максимально реюзабельными. Привожу методы и поля, которые нужно переписать, чтобы подогнать под свой вариант:

* Mark.isInside — логика включения точки в область.
* MainController.x\_values и MainController.y_values — точки, перечисленные в соответствующих листах в приложении.
* Graphic.drawGraphic — изображение области на графике.
* Graphic.BG_COLOR и Graphic.FIGURE_COLOR — цвета фона и заливки графика.
* XPanel, YPanel и RadiusPanel — нужно подогнать под свои элементы управления.

С анимацией всё немного сложнее. Так как триггером запуска анимации являются различные события, то и запускать её нужно по-разному. В целом, всё сводится к одному: в MarkCollection после каждого действия производится какая-то проверка (MarkCollection.check), после чего происходит оповещение графика (Graphic.Notify) и там в отдельном потоке запускается нужная анимация.
