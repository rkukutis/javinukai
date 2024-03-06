import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import toast from "react-hot-toast";
import Button from "../Button";
import updateUser from "../../services/users/updateUser";
import useUserStore from "../../stores/userStore";
import { useNavigate } from "react-router-dom";
import deleteUser from "../../services/users/deleteUser";
import { useTranslation } from "react-i18next";

export function DangerZone({ userData }) {
  console.log(userData);
  const { t } = useTranslation();
  const { user } = useUserStore((state) => state);
  const updateUserMutation = useMutation({
    mutationFn: (data) => updateUser(data),
    onSuccess: () => {
      toast.success(t("DangerZone.permissionsChangedSuccess"));
      queryClient.invalidateQueries(["user"]);
    },
  });
  const deleteUserMutation = useMutation({
    mutationFn: (data) => deleteUser(data),
    onSuccess: () => {
      toast.success(
        t("DangerZone.deletedSuccess", {
          username: `${userData.name} ${userData.surname} (${userData.email})`,
        })
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
      toast.error(t("DangerZone.newRoleSame"));
      return;
    }
    if (
      !confirm(t("DangerZone.changeRole", { newRole: t(`roles.${newRole}`) }))
    )
      return;
    updateUserMutation.mutate({ ...userData, role: newRole });
  }

  function handleToggleLockAccount() {
    if (
      !confirm(
        t("DangerZone.userBlock", {
          action: userData.isNonLocked ? "" : t("DangerZone.unTranslation"),
        })
      )
    )
      return;
    updateUserMutation.mutate({
      ...userData,
      isNonLocked: !userData.isNonLocked,
    });
  }

  function handleDeleteAccount() {
    if (!confirm(t("DangerZone.userDelete"))) return;
    deleteUserMutation.mutate(userData.id);
  }

  return (
    <>
      {user.id === userData.id ? null : (
        <div className="bg-red-50 w-full px-6 py-4 flex flex-col space-y-3 rounded-md">
          <h1 className="text-red-500 text-xl text-center lg:text-left">
            {t("DangerZone.title")}
          </h1>
          <div className="flex flex-col lg:flex-row lg:items-center lg:justify-left">
            <div className="lg:mr-2 p-2 flex flex-col lg:flex-row lg:items-center lg:space-x-4 space-y-2">
              <label className="text-lg text-center">
                {t("DangerZone.changeUserTitle")}
              </label>
              <select
                className="py-2 px-2 rounded-md bg-red-100"
                value={newRole}
                onChange={(e) => setNewRole(e.target.value)}
              >
                <option value="ADMIN">{t("DangerZone.admin")}</option>
                <option value="MODERATOR">{t("DangerZone.moderator")}</option>
                <option value="JURY">{t("DangerZone.jury")}</option>
                <option value="USER">{t("DangerZone.user")}</option>
              </select>
              <Button
                extraStyle="bg-red-400 hover:bg-red-300"
                onClick={handleChangeRole}
              >
                {t("DangerZone.changeUserTitle")}
              </Button>
            </div>
            <div className="p-2 lg:border-l-2 border-red-300 flex flex-col lg:flex-row lg:items-center lg:space-x-2 space-y-2">
              <label className="text-lg text-center">
                {t("DangerZone.accountActionsTitle")}
              </label>
              <Button
                onClick={handleToggleLockAccount}
                extraStyle="bg-red-400 hover:bg-red-300"
              >
                {userData?.isNonLocked
                  ? t("DangerZone.blockAccount")
                  : t("DangerZone.unblockAccount")}
              </Button>
              <Button
                onClick={handleDeleteAccount}
                extraStyle="bg-red-400 hover:bg-red-300"
              >
                {t("DangerZone.deleteAccountButton")}
              </Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
