CREATE TABLE IF NOT EXISTS public.taxi_data_table (
	count int4 NOT NULL,
	avg_distance float8 NULL,
	max_distance float8 NULL,
	min_distance float8 NULL,
	stddev_pop_distance float8 NULL,
	location_id int4 NOT NULL
);