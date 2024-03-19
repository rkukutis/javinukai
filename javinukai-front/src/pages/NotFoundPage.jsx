import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

function NotFoundPage() {
  const navigate = useNavigate();

  function returnToHomepage() {
    navigate("/");
  }

  const { t } = useTranslation();
  return (
    <div className=" w-full h-screen flex flex-col items-center justify-top bg-gray-100">
      <div className="text-6xl font-bold m-8 text-red-600">404</div>
      <h1 className="text-3xl font-bold mb-4">
        {t("NotFoundPage.notFoundInfoTitle")}
      </h1>
      <section className="text-2xl text-center">
        {t("NotFoundPage.notFoundInfoMessage")}
      </section>
      <section
        onClick={() => returnToHomepage()}
        className="mt-8 text-lg text-blue-800 cursor-pointer"
      >
        {t("NotFoundPage.backToHomepage")}
      </section>
    </div>
  );
}

export default NotFoundPage;
