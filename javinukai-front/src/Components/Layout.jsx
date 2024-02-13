import { Outlet } from "react-router-dom";
import LikeButton from "./LikeButton";
import NavBar from "./NavBar";
import Button from "./Button";

function Layout() {
  return (
      <div>
      <header className="text h-[12vh] bg-teal-600 text-slate-100 flex justify-between items-center">
      HEADER
        
        <div className="flex justify-end">
        <a href="/login" className=" mr-2 text-black text-lg justify-end flex pt-4 "> LOGIN</a>
        
        <a href="/register" className="mr-4 text-black text-lg justify-end flex pt-4 ">REGISTER</a>
        </div>
      </header>
      <main className="min-h-[82vh] pb-10">
        <NavBar />
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-teal-600 text-slate-100 ">FOOTER</footer>
    </div>
  );
}

export default Layout;
