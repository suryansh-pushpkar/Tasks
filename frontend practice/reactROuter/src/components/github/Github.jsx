import React from "react";
import { useState } from "react";
import { useEffect } from "react";
import { useLoaderData } from "react-router-dom";

function Github() {
  const data = useLoaderData();

  //   const [data, setData] = useState([]);
  //   useEffect(() => {
  //     console.log("hesl");

  //     fetch("https://api.github.com/users/Suryansh2602")
  //       .then((response) => response.json())
  //       .then((data) => {
  //         console.log(data);
  //         setData(data);
  //       });
  //   }, []);

  return (
    <div className="text-center m-4 p-4 text-3xl  bg-gradient-to-r from-purple-500 to-blue-500 m-30 rounded-t-md">
      Github Followers: {data.followers}
      <img
        className="flex justify-center m-auto rounded-t-md"
        src={data.avatar_url}
        alt=""
        width={300}
      />
    </div>
  );
}

export default Github;

export const githubInfoLoader = async () => {
  const res = await fetch("https://api.github.com/users/Suryansh2602");
  return res.json();
};
