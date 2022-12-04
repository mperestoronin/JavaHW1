# JavaHW1
* Как работает откат хода?
<br>Для удобства использования я по-разному реализовал работу системы отмены хода.
<br>В режиме игрок против игрока, число ходов на которые вы хотите откатиться будет соответствовать количеству отмененных ходов.
<br>Так, например вы сделали ход, решили пойти назад на 3 хода, ввели число 3. Программа сначала отменит ваш последний ход, затем ход вашего соперника, потом опять ваш ход.
<br>В режиме игрок против компьютера, число ходов на которые вы хотите откатиться, не будет учитывать ход компьютера т.к. его бессмысленно откатывать. 
<br>Все тот же пример с отменой 3 ходов: Сначала отменяется ваш ход, потом ход компьютера, потом ваш ход, потом ход компьютера, потом ваш ход. Т. е. по факту, мы <br>вернулись на 5 ходов.
<br>Мне такая система показалась гораздо удобней и интуитивней, чем та, что была использована в режиме игрок против игрока.

* В работе реализован весь требуемый функционал за исключением продвинутого режима.


 * Программа написанна на Oracle Openjdk 19.0.1, Build System: intelliJ
