package cm.packagemanager.pmanager.ws.controller.rest;




public class UserControllerTemplate {//NB: pas de logique métier dans le contrôleur, mais, uniquement l'appel des services

/*	@Autowired
	private UserService userService;

	@GetMapping(value = "/users")
	public ResponseEntity<Collection<User>> getAllUsers() {
		Collection<User> users = userService.getAllUsers();
		return new ResponseEntity<Collection<User>>(users, HttpStatus.FOUND);
	}

	@PostMapping(value = "/users")
	@Transactional
	public ResponseEntity<User> saveUser(@RequestBody User user) {

		User userSaved = userService.saveOrUpdateUser(user);
		return new ResponseEntity<User>(userSaved, HttpStatus.CREATED);
	}

	@PutMapping(value = "/users")
	public ResponseEntity<UserVO> updateUser(@RequestBody User user) {
		User userUpdated = userService.saveOrUpdateUser(user);
		return new ResponseEntity<User>(userUpdated, HttpStatus.OK);
	}

	@DeleteMapping(value = "/users")
	public ResponseEntity<Void> deleteUser(@RequestParam(value = "id", required = true) Long id) throws BusinessResourceException {

		userService.deleteUser(id);
		return new ResponseEntity<Void>(HttpStatus.GONE);
	}

	@PostMapping(value = "/users/login")
	public ResponseEntity<User> findUserByLoginAndPassword(@RequestBody User user) {
		Optional<User> userFound = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
		return new ResponseEntity<User>(userFound.get(), HttpStatus.FOUND);
	}

	@GetMapping(value = "/users/{id}")
	public ResponseEntity<User> findUserById(@PathVariable(value = "id") Long id) {
		Optional<User> userFound = userService.findUserById(id);
		return new ResponseEntity<User>(userFound.get(), HttpStatus.FOUND);
	}*/
}