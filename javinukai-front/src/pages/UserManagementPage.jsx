import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import getUsers from "../services/users/getUsers";
import PaginationSettings from "../Components/PaginationSettings";
import { UserListItem } from "../Components/user-management/UserListItem";
import { BarLoader } from "react-spinners";
import { useTranslation } from "react-i18next";
import ChangePage from "../Components/user-management/ChangePage";
import Button from "../Components/Button";
import { useNavigate } from "react-router-dom";

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "surname",
  sortDesc: "false",
  searchedField: null,
};

export default function UserManagementPage() {
  const navigate = useNavigate();
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching } = useQuery({
    queryKey: [
      "users",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getUsers(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });

  const { t } = useTranslation();

  return (
    <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
      <div className="xl:w-3/4 w-full px-2">
        <PaginationSettings
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
          limitObjectName={t("UserManagementPage.userLimitObject")}
          sortFieldOptions={
            <>
              <option value="name">
                {t("UserManagementPage.userNameOption")}
              </option>
              <option value="surname">
                {t("UserManagementPage.userSurnameIption")}
              </option>
              <option value="email">
                {t("UserManagementPage.userEmailOption")}
              </option>
              <option value="role">
                {t("UserManagementPage.userRoleOption")}
              </option>
              <option value="maxTotal">
                {t("UserManagementPage.userMaxTotalOption")}
              </option>
              <option value="maxSinglePhotos">
                {t("UserManagementPage.userMaxSingleOption")}
              </option>
              <option value="maxCollections">
                {t("UserManagementPage.userMaxCollectionsOption")}
              </option>
              <option value="isEnabled">
                {t("UserManagementPage.userIsEnabledOption")}
              </option>
              <option value="isNonLocked">
                {t("UserManagementPage.userisNonLockedOption")}
              </option>
            </>
          }
          searchByFieldName={t("UserManagementPage.userSeachFieldName")}
          firstPage={data?.firs}
          lastPage={data?.last}
        />
        <div>
          <Button
            onClick={() => navigate("/create-user")}
            extraStyle="bg-red-400 hover:bg-red-300 mb-2"
          >
            {t("UserManagementPage.addNewUserButton")}
          </Button>
        </div>
        <div className="hidden xl:grid xl:grid-cols-10 px-3 py-5 font-bold text-lg text-slate-700 bg-white mt-2 rounded-md shadow">
          <p>{t("UserManagementPage.userNameOption")}</p>
          <p>{t("UserManagementPage.userSurnameIption")}</p>
          <p className="text col-span-3">
            {t("UserManagementPage.userEmailOption")}
          </p>
          <p className="text col-span-2">
            {t("UserManagementPage.userRoleOption")}
          </p>
          <p>{t("UserManagementPage.limits")}</p>
          <p className="text-center break-all">
            {t("UserManagementPage.userIsEnabledOption")}
          </p>
          <p className="text-center break-all">
            {t("UserManagementPage.userisNonLockedOption")}
          </p>
        </div>
        {isFetching ? (
          <div className="h-[50vh] flex flex-col justify-center items-center">
            <BarLoader />
          </div>
        ) : (
          <>
            {data?.content.map((user) => (
              <UserListItem key={user.id} userInfo={user} />
            ))}
          </>
        )}
      </div>
      <div>
        <ChangePage
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={data?.totalPages}
        />
      </div>
    </div>
  );
}
