(function () {
  if (!window.jpptk) {
    function ThirukkuralReader() {

    }

    function Couplet(_id, chapter_id, couplet) {
      this._id = _id;
      this.chapter_id = chapter_id;
      this.couplet = couplet;
    }

    Couplet.prototype.getSQLiteValue = function () {
      return '(' + this._id + ', ' + this.chapter_id + ', "' + this.couplet + '")';
    };

    ThirukkuralReader.prototype.readCurrentChapter = function () {
      var op = {
        couplets: [],
        sqliteStmt: ''
      };

      op.sqliteStmt = 'INSERT INTO couplets (_id, chapter_id, couplet) VALUES ';

      var chapterId = Number($($('#latest ul li>a')[0]).attr('href').split('&')[1].split('=')[1]);
      $('#latest ul li>a').each(function (i, k) {
        var text = k.text;
        var c = new Couplet((chapterId - 1) * 10 + i + 1, chapterId, text);
        op.couplets.push(c);
      });

      op.sqliteStmt += op.couplets.map(function (o) {
        return o.getSQLiteValue();
      }).join(' ') + ";";

      return op;
    };

    jpptk = new ThirukkuralReader();
  }
})();

jpptk.readCurrentChapter().sqliteStmt;
