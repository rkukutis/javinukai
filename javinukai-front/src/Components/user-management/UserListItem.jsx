import { Link } from "react-router-dom";

export function UserListItem({ userInfo }) {
  return (
    <Link
      to={userInfo.uuid}
      className="flex py-4 shadow bg-white lg:px-3 border-white border-4 transition ease-in-out hover:border-teal-400 hover:border-4 hover:cursor-pointer my-2 rounded-md"
    >
      <div className="lg:grid lg:grid-cols-6 px-3 lg:px-0 w-full flex justify-between">
        <div>
          <span className="">{userInfo.name}</span>{" "}
          <span className="lg:hidden inline">{userInfo.surname}</span>
          <p className="lg:hidden block">{userInfo.email}</p>
        </div>
        <p className="lg:block hidden">{userInfo.surname}</p>
        <p className="lg:block hidden">{userInfo.email}</p>
        <p className="lg:flex items-center justify-left">{userInfo.role}</p>
        <p className="hidden lg:flex items-center justify-left">
          {userInfo.maxTotal}/{userInfo.maxSinglePhotos}/
          {userInfo.maxCollections}
        </p>
        <p className="hidden lg:flex items-center justify-left">
          {userInfo.isEnabled ? "Yes" : "No"}
        </p>
      </div>
    </Link>
  );
}
