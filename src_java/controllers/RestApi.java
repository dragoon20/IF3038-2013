package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Comment;

/**
 * Servlet implementation class RestApi
 */
@WebServlet("/RestApi")
public class RestApi extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private HttpSession session;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestApi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		proccessRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		proccessRequest(request, response);
	}
	
	private void proccessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		session = request.getSession();
		String api = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/")+1, request.getRequestURL().length());
		
		Class<?>[] param_handler = new Class[2];
		param_handler[0] = HttpServletRequest.class;
		param_handler[1] = HttpServletResponse.class;
		
		try {
			Method method;
			method = this.getClass().getMethod(api, param_handler);				
			method.invoke(this, request, response);
		} catch (NoSuchMethodException e) {
			// TODO Redirect to error page
			System.out.println("-----------------");
			System.out.println(api);
			System.out.println("-----------------");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fetch_latest_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("task_id")!=null)
		{
			if (request.getParameter("category_id")!=null)
			{
				// TODO check session current user
				// retrieve based on category
				//$ret = Task::model()->findAll("task_id > ".$params['task_id']." AND EXISTS (SELECT * FROM ".Categoory::tableName().
				//	" WHERE category_id = " . $params['category_id']." AND task_id = task.id)");
			}
			else
			{
				// retrieve all
				//$ret = Task::model()->findAll("task_id > ".$params['task_id']);
			}
		}
		// print result
		//return $ret;
	}
	
	public void retrieve_tags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("tags")!=null)
		{
			// retrieve based on existing tags
			/*$condition = "";
			foreach($params['tags'] as $tag)
			{
				$condition .= "tag_name != ".$tag." ";
			}
			$ret = Tag::model()->findAll($condition);*/
		}
		else
		{
			// retrieve all
			//$ret = Tag::model()->findAll();
		}
		// print result
		//return $ret;
	}
	
	/**
	 * Get list of users search by username
	 * @return array of users with likability
	 */
	public void get_tag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("tag")!=null) && (MainApp.LoggedIn(session)))
		{	
			String[] tags = request.getParameter("tag").split(",");
			/*$not_query = array();
			for ($i=0;$i<count($tags)-1;++$i)
			{
				$not_query [] = " tag_name <> '".addslashes($tags[$i])."' ";
			}
			$tag = $tags[count($tags)-1];
			$string = ($not_query) ? " AND ".implode("AND",$not_query) : "";
			$return = Tag::model()->findAll(" tag_name LIKE '".addslashes($tag)."%' ".$string." LIMIT 10");*/
		}
		
		// print result
		//return $return;
	}
	
	
	public void retrieve_users(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//$ret = array();
		if (request.getParameter("users")!=null)
		{
			// retrieve based on existing users
			StringBuilder condition = new StringBuilder();
			/*foreach($params['users'] as $user)
			{
				$condition .= "username != ".$user." ";
			}
			$ret = User::model()->findAll($condition, array("username"));*/
		}
		else
		{
			// retrieve all
			//$ret = User::model()->findAll("", array("username"));
		}
		
		// print result
		//return $ret;
	}
	
	/*** ----- START OF TASK MODULE -----***/
	
	/**
	 * Delete a task
	 * @return string contains whether success or fail
	 */
	public void delete_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ("POST".equals(request.getMethod().toUpperCase()))
		{
			// todo use prepared statement
			String id_task = request.getParameter("task_id");
			boolean success = false;
	
			/*if ((Task::model()->find("id_task=".$id_task)->getDeletable($this->app->currentUserId))&& ($this->app->loggedIn))
			{
				if (Task::model()->delete("id_task=".$id_task)==1)
				{
					// delete was success
					success = true;
				}
				else {
					success = false;
				}
			}*/
		}

		// print result
		/*return array(
			'success' => $success,
			'taskID' => $id_task
		);*/
	}
	
	/**
	 * Retrieve list of tasks for dashboard
	 * @return array of tasks
	 */
	public void retrieve_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//$ret = array();
		if (request.getParameter("category_id")!=null)
		{
			// TODO check if user session is in some category
			// retrieve based on category
			/*$cat = Category::model()->find("id_kategori = '" . addslashes($params['category_id']) . "'");

			if ($cat) 
			{
				// found
				$success = true;

				$categoryID = $cat->id_kategori;
				$categoryName = $cat->nama_kategori;
				$canDeleteCategory = $cat->id_user == $this->app->currentUserId;
				$canEditCategory = $cat->getEditable($this->app->currentUserId);

				$ret = Task::model()->findAll("id_kategori = '" . (int) $params['category_id'] . "'");
				//$canAddTask = Category::model()->find("id_kategori = '". $cat->id_kategori."' AND "$this->app->currentUserId;
			}
			else {
				// not found
				$success = false;
				$ret = array();
			}*/
		}
		else {
			// retrieve all
			/*$success = true;
			$id = $this->app->currentUserId;
			$ret = Task::model()->findAll("id_kategori IN ( SELECT id_kategori FROM ".Category::tableName()." WHERE id_user='$id' ".
											"OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user='$id') ".
											"OR id_kategori IN (SELECT id_kategori FROM ". Task::tableName() ." AS t LEFT OUTER JOIN assign AS a ".
											"ON t.id_task=a.id_task WHERE t.id_user = '". $id ."' OR a.id_user = '". $id ."' ))");*/
		}

		/*$tasks = array();
		foreach ($ret as $task) 
		{
			$dummy = new StdClass;
			$dummy->name = $task->nama_task;
			$dummy->id = $task->id_task;
			$dummy->done = (bool) $task->status;
			$dl = new DateTime($task->deadline);
			$dummy->deadline = $dl->format('j F Y');

			$dummy->deletable = $task->getDeletable($this->app->currentUserId);

			$tags = $task->getTags();
			$dummy->tags = array();
			foreach ($tags as $tag) {
				$dummy->tags[] = $tag->tag_name;
			}

			$tasks[] = $dummy;
		}*/

		// print result
		//return compact('success', 'tasks', 'categoryID', 'categoryName', 'canDeleteCategory', 'canEditCategory');
	}

	public void get_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("id_task")!=null)
		{
			// todo use prepared statement
			String id_task = request.getParameter("id_task");
			//$task = array();
	
			if (MainApp.LoggedIn(session))
			{
				/*$task = Task::model()->find("id_task=".$_GET['id_task'], array("id_task","nama_task","status","deadline"));
	
				$users = $task->getAssignee();
				$temp = array();
				$i = 0;
				foreach ($users as $user)
				{
					$temp[]['username'] = $user->username;
					$temp[$i]['id_user'] = $user->id_user;
					$i++;
				}
				$task->asignee = $temp;
				
				$tags = $task->getTags();
				$temp = array();
				foreach ($tags as $tag)
				{
					$temp[] = $tag->tag_name;
				}
				$task->tag = $temp;*/
			}
			
			// print result
			//return $task->data;
		}
	}

	public void mark_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*$start = microtime();
		$id_task = addslashes($_POST['taskID']);
		$completed = $_POST['completed'] == 'true' ? 1 : 0;

		// TODO permissions

		$update = "UPDATE task SET status=$completed WHERE id_task='$id_task'";

		$q = DBConnection::DBquery($update);
		if (DBConnection::affectedRows()) {
			return array('success' => true, 'taskId' => $id_task, 'done' => $completed);
		}
		else {
			return array('success' => false, $update);
		}*/
	}
	
	/*** ----- END OF TASK MODULE -----***/
	
	/*** ----- START OF CATEGORY MODULE -----***/

	/**
	 * Retrieve list of category
	 * @return array of categories
	 */
	public void retrieve_categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO use categories by user
		//$cats = array();
		if (MainApp.LoggedIn(session))
		{
			/*$raw = $this->app->currentUser->getCategories();

			foreach ($raw as $cat) {
				$dummy = new StdClass;
				$dummy->name = $cat->nama_kategori;
				$dummy->id = $cat->id_kategori;
				$dummy->canDeleteCategory = $cat->getDeletable($this->app->currentUserId);
				$dummy->canEditCategory = $cat->getEditable($this->app->currentUserId);

				$cats[] = $dummy;
			}*/
		}

		// print result
		//return $cats;
	}
	
	/**
	 * Add a new category
	 * @return array of new category data
	 */
	public void add_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			if ("POST".equals(request.getMethod().toUpperCase()))
			{
				String nama_kategori = request.getParameter("nama_kategori");
				/*id_user = $this->app->currentUserId; // the creator of the category
	
				$category = Category::model();
				$category->nama_kategori = $nama_kategori;
				$category->id_user = $id_user;
				$category->save();
	
				$usernames = $_POST['usernames']; // an array of usernames, if using facebook-style
				$usernames_list = $_POST['usernames_list'];
				if (!$usernames && $usernames_list) {
					$usernames = explode(';', $usernames_list);
				}
				foreach ($usernames as $k => $v) {
					$usernames[$k] = trim($v);
				}
				if ($usernames) {
					// Find the IDs of the users
					$escapedUsernames = array();
					foreach ($usernames as $k => $v) {
						$escapedUsernames[] = "'" . addslashes($v) . "'";
					}
					$escapedUsernames = implode(',', $escapedUsernames);
					$escapedUsernames = '(' . $escapedUsernames . ')';
	
					$q = "username IN $escapedUsernames";
					$users = User::model()->findAll($q);
	
					// Insert into relationship table
					foreach ($users as $user) {
						$insert = "INSERT INTO edit_kategori (id_user, id_katego) VALUES ({$user->id_user}, {$category->id_kategori})";
						DBConnection::DBQuery($insert);
					}
				}*/
	
				// print result
				//return array('categoryID' => $category->id_kategori, 'categoryName' => $category->nama_kategori, 'categories' => $this->retrieve_categories());
			}
		}
		//return array();
	}

	/**
	 * Delete a category
	 * @return string contains whether success or fail
	 */
	public void delete_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*$id_kategori = addslashes($_POST['category_id']);
		$success = false;

		if ((Category::model()->find("id_kategori=".$id_kategori)->getDeletable($this->app->currentUserId))&& ($this->app->loggedIn))
		{
			if (Category::model()->delete("id_kategori=".$id_kategori)==1)
			{
				// delete was success
				$success = true;
			}
			else {
				$success = false;
			}
		}

		return array(
			'success' => $success,
			'categoryID' => $id_kategori
		);*/
	}

	/*** ----- END OF CATEGORY MODULE -----***/
	
	/*** ----- START OF COMMENT MODULE -----***/
	
	/**
	 * Get the previous comment from before timestamp
	 * @return array of comments before timestamp
	 */
	public void get_previous_comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("id_task")!=null) && (request.getParameter("timestamp")!=null) && (MainApp.LoggedIn(session)))
		{
			//$return = Comment::getOlder($params['id_task'], $params['timestamp']);
		}
		// print result
		//return $return;
	}
	
	/**
	 * Retrieve comment after timestamp
	 * @return array of comments after timestamp
	 */
	public void retrieve_comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("id_task")!=null) && (request.getParameter("timestamp")!=null) && (MainApp.LoggedIn(session)))
		{
			//$return = Comment::getLatest($params['id_task'], $params['timestamp']);
		}
		
		// print result
		//return $return;
	}
	
	/**
	 * Post a new comment
	 * @return string contains whether success or fail
	 */
	public void comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//$return = "fail";
		if (("POST".equals(request.getMethod())) && (request.getParameter("komentar")!=null) && (MainApp.LoggedIn(session)))
		{
			Comment commment = new Comment();
			/*$comment->data = $params;
			$comment->id_user = $this->app->currentUserId;
			if ($comment->save())
			{
				$return = "success";
			}*/
		}
		
		// print result
		//return $return;
	}
	
	/**
	 * Remove a comment
	 * @return string contains whether success or fail
	 */
	public void remove_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//$return = "fail";
		if (("POST".equals(request.getMethod())) && (request.getParameter("id")!=null) && (MainApp.LoggedIn(session)))
		{
			/*if (Comment::model()->delete("id_komentar = ".addslashes($params['id'])." AND id_user = ".addslashes($this->app->currentUserId))==1)
			{
				$return = "success";
			}*/
		}
		
		// print result
		//return $return;
	}
	
	/*** ----- END OF COMMENT MODULE -----***/
	
	/*** ----- START OF USER MODULE -----***/
	
	/**
	 * Get list of users search by username
	 * @return array of users with likability
	 */
	public void get_username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("username")!=null) && (MainApp.LoggedIn(session))) 
		{
			String[] users = request.getParameter("username").split(",");
			/*$not_query = array();
			for ($i=0;$i<count($users)-1;++$i)
			{
				$not_query [] = " username <> '".addslashes($users[$i])."' ";
			}
			$user = $users[count($users)-1];
			$string = ($not_query) ? " AND ".implode("AND",$not_query) : "";
			$return = User::model()->findAll(" username LIKE '".addslashes($user)."%' ".$string." LIMIT 10", array("id_user", "username"));*/
		}

		// print result
		//return $return;
	}
	
	/**
	 * Login using username and password through Rest API
	 * @return string contains whether success or fail
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (("POST".equals(request.getMethod().toUpperCase())) && 
				(request.getParameter("username")!=null) && (request.getParameter("password")!=null))
		{
			/*$user = User::model()->find("username='".addslashes($params['username'])."' AND password='".md5($params['password'])."'");
			if ($user->data)
			{
				$_SESSION['user_id'] = $user->id_user;
				$u = new User;
				$u->id_user = $user->id_user;
				$u->fullname = $user->fullname;
				$u->username = $user->username;
				$u->email = $user->email;

				$_SESSION['current_user'] = $u;

				$return["status"] = "success";
			}
			else {
				$return["status"] = "fail";
			}
			*/
		}
		else 
		{
			//$return["status"] = "fail";
		}
		
		// print result
		//return $return;
	}
	
	/**
	 * Check register parameter through Rest API
	 * @return array of status and possible errors
	 */
	public void register_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ("POST".equals(request.getMethod().toUpperCase()))
		{
			/*if (($_SERVER['REQUEST_METHOD'] === 'POST') && (ISSET($params['username'])) && (ISSET($params['email']))
					&& (ISSET($params['password'])) && (ISSET($params['confirm_password'])) 
					&& (ISSET($params['fullname'])) && (ISSET($params['birthdate']))
					&& (ISSET($params['avatar']))*/
			/*
			 * $return["status"] = "success";
			$return["error"] = array();
			
			$user = new User();
			$user->data = $params;
			$temperror = $user->checkValidity();
			
			if ($temperror)
			{
				$return["status"] = "fail";
				$return["error"] = array_merge($return["error"], $temperror);
			}
			
			if (User::model()->find("username='".addslashes($params['username'])."' OR email='".addslashes($params['email'])."'")->data)
			{
				$return["status"] = "fail";
				$return["error"]["duplicate"] .= "Username/email sudah digunakan.";
			}
			 */
		}
		else
		{
			//$return["status"] = "fail";
		}

		// print result
		//return $return;
	}

	/*** ----- END OF USER MODULE -----***/	

	/*** ----- START OF SEARCH MODULE -----***/
	public void search_suggestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		$type = $params['type'];
		$q = $params['q'];

		if (!$q)
			return array();

		$all = $type == 'all';

		$suggestions = array();

		if ($type == 'task' || $all) {
			$tasks = $this->app->currentUser->getTasksLike($q);
			foreach ($tasks as $task) {
				if (!in_array($task->nama_task, $suggestions)) {
					$suggestions[] = $task->nama_task;
				}
			}
		}
		if ($type == 'category' || $all) {
			$cats = $this->app->currentUser->getCategoriesLike($q);
			foreach ($cats as $cat) {
				if (!in_array($cat->nama_kategori, $suggestions)) {
					$suggestions[] = $cat->nama_kategori;
				}
			}
		}
		if ($type == 'user' || $all) {
			$users = User::model()->findAllLike($q);
			foreach ($users as $u) {
				if (!in_array($u->username, $suggestions)) {
					$suggestions[] = $u->username;
				}
			}
		}

		return $suggestions;*/
	}
}
