package wang.igood.gmvc.test;

import wang.igood.gmvc.action.result.ActionResult;
import wang.igood.gmvc.action.result.MethodActionResult;
import wang.igood.gmvc.annotation.Path;
import wang.igood.gmvc.common.Controller;

@Path("/home")
public class HomeController extends Controller{

	@Path("/index")
	public ActionResult index() {
		return new MethodActionResult("/admin/index");
	}
}
