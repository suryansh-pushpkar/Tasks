import React from "react";
import { useParams } from "react-router-dom";

function User() {
  const { id } = useParams();
  return <div className=" justify-center flex bg-shadow-md ">User: {id}</div>;
}

export default User;
