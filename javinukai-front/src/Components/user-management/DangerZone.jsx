import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import toast from "react-hot-toast";
import Button from "../Button";
import updateUser from "../../services/users/updateUser";
import useUserStore from "../../stores/userStore";
import { useNavigate } from "react-router-dom";
import deleteUser from "../../services/users/deleteUser";

export function DangerZone({ userData }) {
  const { user } = useUserStore((state) => state);
  const updateUserMutation = useMutation({
    mutationFn: (data) => updateUser(data),
    onSuccess: () => {
      toast.success("User permissions changed");
      queryClient.invalidateQueries(["user"]);
    },
  });
  const deleteUserMutation = useMutation({
    mutationFn: (data) => deleteUser(data),
    onSuccess: () => {
      toast.success(
        `User ${userData.name} ${userData.surname} (${userData.email}) deleted successfully`
      );
      queryClient.invalidateQueries(["users"]);
      navigate("/manage-users");
    },
  });
  const [newRole, setNewRole] = useState(userData.role);
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  function handleChangeRole() {
    if (newRole == userData.role) {
      toast.error("New role is the same as current one");
      return;
    }
    if (
      !confirm(
        `You are about to change this user's role to ${newRole}. Are you sure?`
      )
    )
      return;
    updateUserMutation.mutate({ ...userData, role: newRole });
  }

  function handleToggleLockAccount() {
    if (
      !confirm(
        `You are about to ${
          userData.isNonLocked ? "" : "un"
        }block this user's account. Are you sure?`
      )
    )
      return;
    updateUserMutation.mutate({
      ...userData,
      isNonLocked: !userData.isNonLocked,
    });
  }

  function handleDeleteAccount() {
    if (
      !confirm(
        `You are about to delete this user's account. This action is permanent. Are you sure?`
      )
    )
      return;
    deleteUserMutation.mutate(userData.uuid);
  }

  return (
    <>
      {user.uuid === userData.uuid ? null : (
        <div className="bg-red-50 w-full px-6 py-4 flex flex-col space-y-3 rounded-md">
          <h1 className="text-red-500 text-xl text-center lg:text-left">
            Danger Zone
          </h1>
          <div className="flex flex-col lg:flex-row lg:items-center lg:justify-left">
            <div className="lg:mr-2 p-2 flex flex-col lg:flex-row lg:items-center lg:space-x-4 space-y-2">
              <label className="text-lg text-center">Change user role</label>
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
                Update Role
              </Button>
            </div>
            <div className="p-2 lg:border-l-2 border-red-300 flex flex-col lg:flex-row lg:items-center lg:space-x-2 space-y-2">
              <label className="text-lg text-center">Account actions</label>
              <Button
                onClick={handleToggleLockAccount}
                extraStyle="bg-red-400 hover:bg-red-300"
              >
                {userData?.isNonLocked ? "Block Account" : "Unblock Account"}
              </Button>
              <Button
                onClick={handleDeleteAccount}
                extraStyle="bg-red-400 hover:bg-red-300"
              >
                Delete Account
              </Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
