import { useState, useEffect } from "react";
import axios from "axios";
import { useTranslation } from "react-i18next";

function ContestPreview() {
  const [contests, setContests] = useState([]);
  const [loading, setLoading] = useState(true);
  const { t } = useTranslation();

  useEffect(() => {
    const fetchContests = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND}/api/v1/contests` , { withCredentials: true }
        );
        setContests(response.data.content);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching contests:", error);
      }
    };

    fetchContests();
  }, []);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-semibold mb-4">{t("ContestPreview.contestPreviewTitle")}</h2>
      {loading ? (
        <p className="text-gray-600">{t("ContestPreview.loadingTitle")}</p>
      ) : (
        <ul>
          {contests.map((contest) => (
            <li key={contest.id} className="border rounded-md p-4 mb-4">
              <h3 className="text-lg font-semibold mb-2">
                {contest.name}
              </h3>
              <p className="text-gray-600 mb-2">{contest.description}</p>
              <p className="text-gray-700 mb-2">
              {t("ContestPreview.totalSubmissionsTitle")}{contest.totalSubmissions}
              </p>
              <p className="text-gray-700 mb-2">
              {t("ContestPreview.startDate")}{new Date(contest.startDate).toLocaleDateString()}
              </p>
              <p className="text-gray-700 mb-2">
              {t("ContestPreview.endDate")}{new Date(contest.endDate).toLocaleDateString()}
              </p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default ContestPreview;
