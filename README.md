# Project Boodleheimer

Because the best monads are those that look like they were written by a 6 year old.

###Motivational Song
```
Boodleheimer! Boodleheimer!
clap, clap, clap
Boodleheimer! Boodleheimer!
clap, clap, clap
The more you boodle, the less you heimer!
The more you heimer, the less you boodle!
Boodleheimer! Boodleheimer!
clap, clap, clap
```

Song from The Silly Book. You can listen to the song [here](https://www.youtube.com/watch?v=eBNvM3VdIKo)
###Whining
Scala's built in futures are composable, and there is some support for error recovery, but when all you want is error reporting,
and you want to chain a bunch of futures, providing a single Future[Either[A,B]] is a lot of code.

We could use scalaz Tasks, but then we have to add scalaz to the project, and the scalaz people dislike code of conducts
and smell of elderberries.

Boodleheimer, as the instructional song clearly indicates, lets you keep boodling and heimering, mapping over successes,
and keeping the first error. To leave the context, we can either clap, which returns a unified type T, or clapclapclap, that
returns the raw Either for you to deal with.
