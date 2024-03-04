import Button from "../Button";
import { useTranslation } from "react-i18next";

export default function ChangePage({
  pagination,
  setPagination,
  availablePageNumber,
}) {
  
  function handleNextPage() {
    if (
      availablePageNumber === 1 ||
      pagination.page === availablePageNumber - 1
    )
      return;
    setPagination({ ...pagination, page: pagination.page + 1 });
  }
  function handlePreviousPage() {
    if (pagination.page === 0) return;
    setPagination({ ...pagination, page: pagination.page - 1 });
  }
  
  const { t } = useTranslation();
  return (
    
      <section className="flex space-x-3 justify-center xl:col-span-4 xl:h-fit xl:self-end my-5">
        <Button disabled={pagination.page == 0} onClick={handlePreviousPage}>
          {t("PaginationSettings.previousPage")}
        </Button>
        <p className="text-xl py-1 px-4 rounded-full bg-white">
          {pagination.page + 1 + "/" + (availablePageNumber || pagination.page)}
        </p>
        <Button
          disabled={
            availablePageNumber === 1 ||
            pagination.page === availablePageNumber - 1
          }
          extraStyle="px-8"
          onClick={handleNextPage}
        >
          {t("PaginationSettings.nextPage")}
        </Button>
      </section>
    
  );
}
