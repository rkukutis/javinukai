import { Link } from "react-router-dom";

export function UserListItem({ userInfo }) {
  return (
    <Link
      to={userInfo.uuid}
      className="flex py-4 shadow bg-white xl:px-3 border-white border-4 transition ease-in-out hover:border-teal-400 hover:border-4 hover:cursor-pointer my-2 rounded-md"
    >
      <div className="xl:grid xl:grid-cols-9 px-3 xl:px-0 w-full flex justify-between">
        <div>
          <span className="">{userInfo.name}</span>{" "}
          <span className="xl:hidden inline">{userInfo.surname}</span>
          <p className="xl:hidden block break-all">{userInfo.email}</p>
        </div>
        <p className="xl:block hidden">{userInfo.surname}</p>
        <p className="xl:block hidden break-all text-wrap col-span-3">
          {userInfo.email}
        </p>
        <p className="xl:flex items-center justify-left">{userInfo.role}</p>
        <p className="hidden xl:flex items-center justify-left">
          {userInfo.maxTotal}/{userInfo.maxSinglePhotos}/
          {userInfo.maxCollections}
        </p>
        <div className="hidden xl:flex items-center justify-center text-white">
          <span
            className={`text px-3 rounded-md ${
              userInfo.isEnabled ? "bg-green-500" : "bg-red-500"
            }`}
          >
            {userInfo.isEnabled ? "Yes" : "No"}
          </span>
        </div>
        <div className="hidden xl:flex items-center justify-center text-white">
          <span
            className={`text px-3 rounded-md ${
              userInfo.isNonLocked ? "bg-green-500" : "bg-red-500"
            }`}
          >
            {userInfo.isNonLocked ? "No" : "Yes"}
          </span>
        </div>
      </div>
    </Link>
  );
}
