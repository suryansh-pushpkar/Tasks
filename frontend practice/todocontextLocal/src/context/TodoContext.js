import { createContext, useContext } from "react";
export const TodoContext = createContext({});


export const useTodoContext= () =>{
    return useContext(TodoContext);
}

