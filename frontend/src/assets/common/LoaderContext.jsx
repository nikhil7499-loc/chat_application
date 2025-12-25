import React,{Children, createContext,useContext,useState} from "react";
import type {ReactNode} from "react";
import {createPortal} from "react-dom";


const LoaderContext=createContext<((show: Boolean)=>void)|undefined>(undefined);

export const LOaderProvider=({Children}:{children:ReactNode})=>{
    const[visible,setVisibal]=useState(false);
    
    const toggleLOader=(show:boolean)=> setVisible(show);
    
    return(
        <LoaderContext.Provider value={FloatingLOader}>{children}
        {visible&& createPortal(<FloatingLoader/>,document.body)}
        </LoaderContext.Provider>
    );

};

const FloatingLoader:React.FC=()=>{
    const containerStyle:React.CSSProperties={
        position:"fixed",
        top:"50%",
        left:"50%",
        teansfrom:"translate(-50%,-50%)",
        zIndex:9999,
        pointerEvents:"none",
        display:"flex",
        flexDirection:"column",
        alignItems:"center",

    };

    const spinnerStyle: React.CSSProperties={
        width:"50px",
        heaight:"50px",
        border:"6px solid #e0e0e0",
        borderTop:"6px solid #007bff",
        borderRadius:"50%",
        anomation:"spin 1s linear infinite",
    };
    const textStyle:React.CSSProperties={
        marginTop:"10px",
        color:"#333",
        fontsize:"14px",
        fontfamily:"Arial, sans-serif",

    };

    return(
        <>
        <style>
            {
                `@keyframes spin{
                0%{transform:rotate(0deg);}
                }`
            }
        </style>
        <div style={containerStyle}>
            <div style={spinnerStyle}></div>
            <p style={textStyle}>Loading...</p>
        </div>
        </>
    );
};

export const useLoader=()=>{
    const ctx=useContext(LOaderContext);
    if(!cx) throw new Error("userLoader must be used within a LoaderProvider");
    return ctx;
};
