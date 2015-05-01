
import scala.concurrent._

class BoodleHeimer[Boodle,Heimer](b: Future[Either[Heimer,Boodle]])(implicit ec:ExecutionContext){
  def boodle[B](f:Boodle => Future[B], h: PartialFunction[Throwable,Heimer]) :BoodleHeimer[B,Heimer] =
    new BoodleHeimer(b.flatMap{
      case Right(x) => f(x).map(Right(_)).recover(h.andThen(Left(_)))
      case Left(x) => Future(Left(x))})

  def heimer[B](f:Boodle => Future[Either[Heimer,B]], h: PartialFunction[Throwable,Heimer]) :BoodleHeimer[B,Heimer] =
    new BoodleHeimer(b.flatMap{
      case Right(x) => f(x).recover(h.andThen(Left(_)))
      case Left(x) => Future(Left(x))})

  def clapclapclap = b

  def clap[A](bf:Boodle =>A, hf : Heimer => A) : Future[A] = {
    b.map{
      case Right(x) => bf(x)
      case Left(x) => hf(x)
    }
  }
}

object BoodleHeimer{
  def apply[Boodle,Heimer](b:Future[Boodle], errorHandler: PartialFunction[Throwable,Heimer])(implicit ec:ExecutionContext) =
    new BoodleHeimer[Boodle,Heimer](b.map(Right(_)).recover(errorHandler.andThen(Left(_))))

}
