import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import toast from "react-hot-toast";
import Button from "../Button";
import updateUser from "../../services/users/updateUser";
import useUserStore from "../../stores/userStore";

function ConfirmationModal({ children, onResponse, onModalSet }) {
  return (
    <div className="absolute top-0 left-0 w-full h-full backdrop-blur-md backdrop-brightness-50 flex flex-col justify-center items-center">
      <div className="lg:w-[20rem] bg-white flex flex-col space-y-4 items-center px-6 py-4 rounded-md">
        <h1 className="text-2xl text-center">{children}</h1>
        <div className="grid grid-cols-2 w-full gap-x-4">
          <Button
            extraStyle="bg-green-500 hover:bg-green-400"
            onClick={() => onResponse(true)}
          >
            Yes
          </Button>
          <Button
            extraStyle="bg-red-500 hover:bg-red-400"
            onClick={() => onResponse(false)}
          >
            No
          </Button>
        </div>
      </div>
    </div>
  );
}

export function DangerZone({ userData }) {
  const { user } = useUserStore((state) => state);
  const { mutate } = useMutation({
    mutationFn: (data) => updateUser(data),
    onSuccess: () => {
      toast.success("User permissions changed");
      queryClient.invalidateQueries(["user"]);
    },
  });
  const [newRole, setNewRole] = useState(userData.role);
  const queryClient = useQueryClient();

  function handleChangeRole() {
    if (newRole == userData.role) {
      toast.error("New role is the same as old one");
      return;
    }
    mutate({ ...userData, role: newRole });
  }

  function handleToggleLockAccount() {
    mutate({ ...userData, isNonLocked: !userData.isNonLocked });
  }

  return (
    <div className="border-2 border-red-300 bg-red-50 w-full px-6 py-4 flex flex-col space-y-3 rounded-md">
      <h1 className="text-red-500 text-xl">Danger Zone</h1>
      {user.uuid === userData.uuid && (
        <h1 className="text-red-500 text-xl font-bold">
          Your are editing your own permissions. Proceed with extreme caution as
          you may lock yourself out!
        </h1>
      )}
      <div className="flex space-x-3 items-center">
        <div className=" border-2 border-red-300 bg-white p-2 rounded-md flex items-center space-x-4">
          <label className="text-lg">User Role</label>
          <select
            className="py-2 px-2 rounded-md bg-red-100"
            value={newRole}
            onChange={(e) => setNewRole(e.target.value)}
          >
            <option value="ADMIN">Admin</option>
            <option value="MODERATOR">Moderator</option>
            <option value="JURY">Jury Member</option>
            <option value="USER">User</option>
          </select>
          <Button
            extraStyle="bg-red-400 hover:bg-red-300"
            onClick={handleChangeRole}
          >
            Update User
          </Button>
        </div>
        <div>
          <Button
            onClick={handleToggleLockAccount}
            extraStyle="bg-red-400 hover:bg-red-300"
          >
            {userData?.isNonLocked ? "Lock Account" : "Unlock Account"}
          </Button>
        </div>
      </div>
    </div>
  );
}
