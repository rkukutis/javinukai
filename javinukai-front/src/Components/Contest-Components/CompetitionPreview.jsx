import React, { useState, useEffect } from "react";
import axios from "axios";

function CompetitionRecordPreview() {
  const [competitionRecords, setCompetitionRecords] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCompetitionRecords = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND}/api/v1/competition-records`
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
      <h2>Competition Records</h2>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <ul>
          {competitionRecords.map((record) => (
            <li key={record.id}>
              <h3>{record.contestName}</h3>
              <p>User: {record.userName}</p>
              <p>Category: {record.categoryName}</p>
              {/* Add more details as needed */}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default CompetitionRecordPreview;
