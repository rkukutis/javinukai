import Button from "./Button";
import { useTranslation } from "react-i18next";

export function JuryViewPagination({
  pagination,
  setPagination,
  availablePageNumber,
  limitObjectName,
  firstPage,
  lastPage,
}) {
  const { t } = useTranslation();
  function handleNextPage() {
    if (lastPage) return;
    setPagination({ ...pagination, page: pagination.page + 1 });
  }
  function handlePreviousPage() {
    if (firstPage) return;
    setPagination({ ...pagination, page: pagination.page - 1 });
  }
  function handleLimitChange(e) {
    setPagination({ ...pagination, limit: e.target.value, page: 0 });
  }
  function handleFilterChange(e) {
    setPagination({ ...pagination, display: e.target.value, page: 0 });
  }

  return (
    <div className="xl:grid xl:grid-cols-3 xl:gap-4 bg-slate-50 rounded">
      <section className="flex flex-col py-2">
        <label className="mb-1">Page</label>
        <div className="flex space-x-3">
          <Button
            extraStyle="w-full"
            disabled={pagination.page == 0}
            onClick={handlePreviousPage}
          >
            {t("PaginationSettings.previousPage")}
          </Button>
          <p className="text-xl py-1 px-4 rounded-full bg-white">
            {pagination.page +
              1 +
              "/" +
              (availablePageNumber || pagination.page)}
          </p>
          <Button
            extraStyle="w-full"
            disabled={
              availablePageNumber === 1 ||
              pagination.page === availablePageNumber - 1
            }
            onClick={handleNextPage}
          >
            {t("PaginationSettings.nextPage")}
          </Button>
        </div>
      </section>
      <section className="flex flex-col py-2">
        <label className="mb-1">
          {limitObjectName} {t("PaginationSettings.perPage")}
        </label>
        <select
          className="py-2 px-4 bg-white rounded-md"
          onChange={handleLimitChange}
          value={pagination.limit}
        >
          <option value={1}>1 {limitObjectName}</option>
          <option value={25}>25 {limitObjectName}</option>
          <option value={50}>50 {limitObjectName}</option>
          <option value={100}>100 {limitObjectName}</option>
        </select>
      </section>
      <section className="flex flex-col py-2">
        <label className="mb-1">Show entries</label>
        <select
          className="py-2 px-4 bg-white rounded-md"
          onChange={handleFilterChange}
          value={pagination.display}
        >
          <option value="all">All {limitObjectName}</option>
          <option value="liked">Only liked {limitObjectName}</option>
          <option value="unliked">Only unliked {limitObjectName}</option>
        </select>
      </section>
    </div>
  );
}
