import org.scalatest.{Matchers, FlatSpec}

import scala.concurrent.{Await, Future}
import scala.util.control.NonFatal
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class BoodleHeimerSpec extends FlatSpec with Matchers{

  it should "do just fine when nothing bad happens" in {

     val boodle =BoodleHeimer(doSlow((x:String) =>"S",100)("S"),handle("Err"))
                      .boodle(doSlow((x:String) => x+x,100),handle("Err2"))

    Await.result(boodle.clap(x=>x,x=>x),4.seconds) should be("SS")
  }


  it should "report errors" in {
    val boodle =BoodleHeimer(doSlow((x:String) =>"S",100)("S"),handle("Err"))
      .boodle(failSlow((x:String) => x+x,100),handle("Err2"))

    Await.result(boodle.clap(x=>x,x=>x),4.seconds) should be("Err2")
  }


  it should "not run anything after the first error" in {
    val boodle =BoodleHeimer(doSlow((x:String) =>"S",100)("S"),handle("Err"))
      .boodle(failSlow((x:String) => x+x,100),handle("Err2"))
      .boodle(failSlow((x:String) => x+x+x,100),handle("Err3"))

    Await.result(boodle.clap(x=>x,x=>x),4.seconds) should be("Err2")
  }


  def handle(s:String) = PartialFunction[Throwable,String]{
    case NonFatal(e) => s
  }

  def doSlow[F,R](f:F=>R, millis:Long): F=>Future[R] = {
    (x:F) =>
    Future{
      Thread.sleep(millis)
      f(x)
    }
  }

  def failSlow[F,R](f:F=>R, millis:Long): F=>Future[R] = {
    (x:F) =>
      Future{
        Thread.sleep(millis)
        throw new RuntimeException("Fail!")
        f(x)
      }
  }
}
