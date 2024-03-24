import { useState, useEffect } from "react";
import axios from "axios";
import { useTranslation } from "react-i18next";

function CompetitionRecordPreview() {
  const [competitionRecords, setCompetitionRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const { t } = useTranslation();

  useEffect(() => {
    const fetchCompetitionRecords = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND}/api/v1/competition-records` , { withCredentials: true }
        );
        console.log("Response:", response);
        setCompetitionRecords(response.data);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching competition records:", error);
      }
    };

    fetchCompetitionRecords();
  }, []);

  return (
    <div>
      <h2>{t("ContestPreview.recordsTitle")}</h2>
      {loading ? (
        <p>{t("ContestPreview.loadingTitle")}</p>
      ) : (
        <ul>
          {competitionRecords.map((record) => (
            <li key={record.id}>
              <h3>{record.contestName}</h3>
              <p>{t("ContestPreview.userTitle")} {record.userName}</p>
              <p>{t("ContestPreview.categoryTitle")} {record.categoryName}</p>
              {/* Add more details as needed */}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default CompetitionRecordPreview;
