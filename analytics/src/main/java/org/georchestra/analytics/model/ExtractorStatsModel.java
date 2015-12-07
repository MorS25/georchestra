package org.georchestra.analytics.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtractorStatsModel extends AbstractModel  {


	private final String selectLayersQ = "SELECT "
	        + "    y.ows_url "
	        + "    , y.ows_type "
	        + "    , y.layer_name "
	        + "    , COUNT(*) AS count "
	        + "FROM "
			+ "    downloadform.extractorapp_log l "
			+ "    , downloadform.extractorapp_layers y "
			+ "WHERE "
			+ "    requested_at >= ?::timestamp "
			+ "AND "
			+ "    requested_at < ?::timestamp "
			+ "AND "
			+ "    l.id = y.extractorapp_log_id "
			+ "GROUP BY "
			+ "    y.layer_name "
			+ "    , y.ows_url "
			+ "    , y.ows_type "
			+ "ORDER BY "
			+ "    @sort@ "
			+ "LIMIT ? OFFSET ?;";

	private final String selectUsersQ = "SELECT "
	        + "    l.username AS username "
	        + "    , COUNT(*) AS count "
	        + "FROM "
	        + "    downloadform.extractorapp_log l "
	        + "    , downloadform.extractorapp_layers y "
	        + "WHERE "
	        + "    requested_at >= ?::timestamp "
	        + "AND "
	        + "    requested_at < ?::timestamp "
	        + "AND "
	        + "    l.id = y.extractorapp_log_id "
	        + "GROUP BY "
	        + "    l.username "
	        + "ORDER BY "
	        + "    @sort@ "
	        + "LIMIT ? OFFSET ?;";

	private final String selectGroupsQ = "SELECT "
	        + "    l.company AS company "
	        + "    , count(*) AS count "
	        + "FROM "
	        + "    downloadform.extractorapp_log l "
	        + "    , downloadform.extractorapp_layers y "
	        + "WHERE "
			+ "    requested_at >= ?::timestamp "
			+ "AND "
			+ "    requested_at < ?::timestamp "
			+ "AND "
			+ "    l.id = y.extractorapp_log_id "
			+ "GROUP BY "
			+ "    l.company "
			+ "ORDER BY "
			+ "    @sort@ "
			+ "LIMIT ? OFFSET ?;";

	public JSONObject getLayersStats(final int month, final int year, final int start, final int limit, final String sort, final String filter) throws SQLException, JSONException {

		return getStats(month, year, start, limit, sort, filter, selectLayersQ, new StrategyModel() {

			protected JSONArray process(ResultSet rs) throws SQLException, JSONException {
				JSONArray jsarr = new JSONArray();
				while (rs.next()) {
					JSONObject res = new JSONObject();
					res.put("ows_type", rs.getString("ows_type"));
					res.put("ows_url", rs.getString("ows_url"));
					res.put("layer_name", rs.getString("layer_name"));
					res.put("count", rs.getInt("count"));
					jsarr.put(res);
			     }
				return jsarr;
			}
		});
	}

	public JSONObject getUsersStats(final int month, final int year, final int start, final int limit, final String sort, final String filter) throws SQLException, JSONException {

		return getStats(month, year, start, limit, sort, filter, selectUsersQ, new StrategyModel() {

			protected JSONArray process(ResultSet rs) throws SQLException, JSONException {
				JSONArray jsarr = new JSONArray();
				while (rs.next()) {
					JSONObject res = new JSONObject();
					res.put("username", rs.getString("username"));
					res.put("count", rs.getInt("count"));
					jsarr.put(res);
			     }
				return jsarr;
			}
		});
	}

	public JSONObject getGroupsStats(final int month, final int year, final int start, final int limit, final String sort, final String filter) throws SQLException, JSONException {

		return getStats(month, year, start, limit, sort, filter, selectGroupsQ, new StrategyModel() {

			protected JSONArray process(ResultSet rs) throws SQLException, JSONException {
				JSONArray jsarr = new JSONArray();
				while (rs.next()) {
					JSONObject res = new JSONObject();
					res.put("company", rs.getString("company"));
					res.put("count", rs.getInt("count"));
					jsarr.put(res);
			     }
				return jsarr;
			}
		});
	}
}
