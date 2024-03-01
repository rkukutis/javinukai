import { useState } from "react";
import Button from "../Button";
import { useTranslation } from "react-i18next";

export default function PaginationSettings({
  pagination,
  setPagination,
}) {
  const [searchedSurname, setSearchedSurname] = useState("");

  function handleLimitChange(e) {
    e.preventDefault();
    setPagination({ ...pagination, limit: e.target.value, page: 0 });
  }
  function handleSortByChange(e) {
    e.preventDefault();
    setPagination({ ...pagination, sortBy: e.target.value });
  }
  function handleSortDescChange(e) {
    e.preventDefault();
    setPagination({ ...pagination, sortDesc: e.target.value });
  }
  function handleSurnameFieldChange(e) {
    setSearchedSurname(e.target.value);
  }

  function handleSurnameSearchKeydown(e) {
    if (e.key !== "Enter") return;
    handleSurnameSearchClick();
  }
  function handleSurnameSearchClick() {
    setPagination({ ...pagination, surnameContains: searchedSurname });
  }

  const { t } = useTranslation();
  return (
    <div className="flex flex-col px-2 py-5 space-y-2 xl:grid xl:grid-cols-4 xl:gap-2">
      
      <section className="flex flex-col space-y-2 py-3">
        <label>{t("PaginationSettings.usersTitle")}</label>
        <select
          className="py-2 px-4 bg-white rounded-md"
          onChange={handleLimitChange}
          value={pagination.limit}
        >
          <option value={10}>10 {t("PaginationSettings.users")}</option>
          <option value={25}>25 {t("PaginationSettings.users")}</option>
          <option value={50}>50 {t("PaginationSettings.users")}</option>
          <option value={100}>100 {t("PaginationSettings.users")}</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.fieldTitle")}</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortByChange}
          value={pagination.sortBy}
        >
          <option value="name">{t("PaginationSettings.fieldName")}</option>
          <option value="surname">
            {t("PaginationSettings.fieldSurname")}
          </option>
          <option value="email">{t("PaginationSettings.fieldEmail")}</option>
          <option value="role">{t("PaginationSettings.fieldRole")}</option>
          <option value="maxTotal">
            {t("PaginationSettings.fieldMaxTotalEntries")}
          </option>
          <option value="maxSinglePhotos">
            {t("PaginationSettings.fieldMaxSingles")}
          </option>
          <option value="maxCollections">
            {t("PaginationSettings.fieldMaxCollections")}
          </option>
          <option value="isEnabled">
            {t("PaginationSettings.fieldIsEnabled")}
          </option>
          <option value="isNonLocked">
            {t("PaginationSettings.fieldIsNonLocked")}         
          </option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.sortTitle")}</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortDescChange}
          value={pagination.sortDesc}
        >
          <option value="true">{t("PaginationSettings.sortDescending")}</option>
          <option value="false">{t("PaginationSettings.sortAscending")}</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.searchTitle")}</label>
        <div className="lg:flex-row flex lg:space-x-3 space-y-2 lg:space-y-0 flex-col">
          <input
            className="p-2 bg-white rounded-md w-full"
            placeholder={t("PaginationSettings.searchPlaceholder")}
            onKeyDown={handleSurnameSearchKeydown}
            onChange={handleSurnameFieldChange}
            value={searchedSurname}
          />
          <Button onClick={handleSurnameSearchClick}>
            {t("PaginationSettings.searchButton")}
          </Button>
        </div>
      </section>
    </div>
  );
}
