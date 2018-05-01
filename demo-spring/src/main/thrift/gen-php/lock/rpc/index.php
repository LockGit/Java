<?php
include 'UserRpc.php';
class User implements lock\rpc\UserRpcIf{

  public function login($name, $psw){

  }
  public function add($name, $psw){

  }
}


$user = new User();

var_export($user->login('admin','admin'));